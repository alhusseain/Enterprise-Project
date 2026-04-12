import requests
import threading
import uuid
import sys
import os

BASE_URL = "http://localhost:8080"
TENANT_FILE = ".tenant_ids.txt"

def run_test():
    # Make sure the app is spun up and the file is there
    if not os.path.exists(TENANT_FILE):
        print(f"Oops! I can't find {TENANT_FILE}. Rerun the app first.")
        sys.exit(1)

    # Grab the tenant ID that we sneakily dumped
    with open(TENANT_FILE, 'r') as f:
        tenant_id = f.read().strip()
    
    print(f"Testing on Tenant ID: {tenant_id}")

    # Whip up a random email so we don't trip over "User Already Exists" errors
    random_email = f"test_{uuid.uuid4().hex[:6]}@example.com"
    password = "password123"

    # Let's register our fake user
    print(f"Registering our test user: {random_email}")
    register_resp = requests.post(f"{BASE_URL}/auth/register", json={
        "email": random_email,
        "password": password,
        "tenantId": tenant_id
    })
    
    if register_resp.status_code not in (200, 201):
        print("Registration failed:", register_resp.text)
        sys.exit(1)

    # log in and grab the keys (JWT token)
    print("Logging in...")
    login_resp = requests.get(f"{BASE_URL}/auth/login", json={
        "email": random_email,
        "password": password
    })
    
    token = login_resp.text.strip()
    headers = {"Authorization": f"Bearer {token}"}

    # Set up the scene: create a blank project
    print("Creating a fresh project...")
    proj_resp = requests.post(f"{BASE_URL}/projects", json={
        "name": "Concurrency Test Project",
        "createdBy": random_email
    }, headers=headers)
    
    project_id = proj_resp.json()["id"]

    # Drop a fresh, untouched task in there
    print("Dropping a task in the project...")
    task_resp = requests.post(f"{BASE_URL}/projects/{project_id}/tasks", json={
        "title": "Explode the DB"
    }, headers=headers)
    
    task_id = task_resp.json()["id"]
    print(f"Task created with ID: {task_id}")

    # 6. The Concurrency Test
    print("\n--- INITIATING CONCURRENT PATCH REQUESTS ---")
    
    results = []
    
    def patch_task(new_status, thread_name):
        try:
            resp = requests.patch(f"{BASE_URL}/tasks/{task_id}", json={
                "status": new_status
            }, headers=headers)
            results.append(f"{thread_name} Finished! Status Code: {resp.status_code}")
        except Exception as e:
            results.append(f"{thread_name} Failed: {str(e)}")

    # Create two threads trying to take the EXACT same task simultaneously
    thread1 = threading.Thread(target=patch_task, args=("IP", "Thread A (Claiming Task)"))
    thread2 = threading.Thread(target=patch_task, args=("IP", "Thread B (Claiming Task)"))

    # Start them at the exact same time
    thread1.start()
    thread2.start()

    # Wait for both to finish
    thread1.join()
    thread2.join()

    print("\n--- RESULTS ---")
    for r in results:
        print(r)
        
    print("\nNotice how one thread succeeds (200 OK), and the other fails, which should be a 5XX error and not 409 since 409 is for when the client is the one who made the mistake transitioning to a state that is not allowed.")
    print("The database rejected the second thread because the version changed, triggering a rollback!")

    # Cleanup the file
    os.remove(TENANT_FILE)
    print("\nCleaned up .tenant_ids.txt")

if __name__ == "__main__":
    run_test()
