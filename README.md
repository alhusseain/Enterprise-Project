# WorkHub  Enterprise Project

A multi-tenant Spring Boot REST API with JWT authentication, project management, and task tracking.

---

## Prerequisites

| Tool | Minimum version |
|------|----------------|
| Java JDK | 21 |
| Maven | 3.9+ *(or use the included wrapper)* |
| IntelliJ IDEA | 2023.1+ (Community or Ultimate) |
| Postman | Any recent version |

## Running on IntelliJ IDEA

### 1. Open the project

1. Launch IntelliJ IDEA.
2. Choose **File → Open** and select the root folder of this repository (`Enterprise-Project/`).
3. IntelliJ will detect the `pom.xml` and import it as a Maven project automatically. If prompted, click **Load Maven Project**.

### 2. Set the Java SDK

1. Go to **File → Project Structure → Project**.
2. Set **SDK** to JDK 21 (add it via **Add SDK → JDK** if not listed).
3. Set **Language level** to `21`. Click **OK**.

### 3. Install dependencies

IntelliJ resolves Maven dependencies automatically on import. To trigger a manual refresh:

1. Open the **Maven** panel (right sidebar).
2. Click the **Reload All Maven Projects** button (circular arrow icon).

If you prefer the terminal, use:

```bash
mvnw.cmd install -DskipTests
```

This downloads all dependencies and builds the project without running tests.

### 4. Run the application

- Locate `WorkHubApplication.java` under `src/main/java/com/example/WorkHub/`.
- Click the green **Run** arrow next to the class declaration, or right-click the file and choose **Run 'WorkHubApplication'**.
- The server starts on **<http://localhost:8080>**.

> **Alternative IntelliJ Maven tool window:**
> Open the **Maven** panel → **Plugins → spring-boot → spring-boot:run** and double-click it.
>
> **Alternative   terminal:**
>
> ```bash
> mvnw.cmd spring-boot:run
> ```

## Fetching Tenant IDs from the H2 Console

On first startup the application automatically seeds **three tenants** into the database:

| Name | Plan |
|------|------|
| Oscorp Industries | BASIC |
| FawryPay | PRO |
| Initech | ENTERPRISE |

You will need a **Tenant ID** when registering a new user. Retrieve them as follows:

1. Open your browser and navigate to **<http://localhost:8080/h2-console>**.
2. Fill in the connection form with these exact values and click **Connect**:

   | Field | Value |
   |-------|-------|
   | Driver Class | `org.h2.Driver` |
   | JDBC URL | `jdbc:h2:~/testdb` |
   | User Name | `sa` |
   | Password | *(leave blank)* |

3. In the SQL editor on the left, run:

   ```sql
   SELECT * FROM TENANT;
   ```

4. Copy any `ID` value from the result   you will paste it as `tenantId` in the register request.

## Importing the Postman Collection

1. Open Postman.
2. Click **Import** (top-left).
3. Choose **File** and select `documents/Enterprise App.postman.json` from this repository.
4. The collection **Enterprise App** will appear in your sidebar with four folders:

   | Folder | Endpoints |
   |--------|-----------|
   | Auth | `POST /auth/register`, `GET /auth/login`, `GET /auth/me` |
   | Project | `POST /projects`, `GET /projects`, `GET /projects/{id}`, `POST /projects/{id}/tasks` |
   | Tasks | `PATCH /tasks/{id}` |
   | System | `GET /health`, `GET /WhatAmI` |

## Typical First-Run Workflow

Follow these steps in order using the imported Postman collection:

### Step 1  Register a user

**Auth → Auth/register** `POST /auth/register`

```json
{
    "email": "you@example.com",
    "password": "yourpassword",
    "tenantId": "<UUID from H2 console>"
}
```

### Step 2  Log in and get your JWT

**Auth → Auth/login** `GET /auth/login`

```json
{
    "email": "you@example.com",
    "password": "yourpassword"
}
```

Copy the JWT token returned in the response body.

### Step 3  Authenticate subsequent requests

For every protected request, set **Authorization → Bearer Token** and paste the JWT you received in Step 2. The existing requests in the collection already show this pattern in the `Authorization` tab.

### Step 4  Create a project

**Project → projects** `POST /projects`

```json
{
    "name": "My First Project",
    "createdBy": "you@example.com"
}
```

### Step 5  Add a task to the project

**Project → projects/{id}/tasks** `POST /projects/{id}/tasks`

Replace `{id}` in the URL with the project ID returned in Step 4.

```json
{
    "title": "My first task"
}
```

### Step 6  Update a task status

**Tasks → tasks/{id}** `PATCH /tasks/{id}`

Replace `{id}` with the task ID returned in Step 5.

```json
{
    "status": "IN_PROGRESS"
}
```

## Configuration Reference (`application.properties`)

| Property | Value |
|----------|-------|
| Server port | `8080` |
| H2 JDBC URL | `jdbc:h2:~/testdb` |
| H2 console path | `/h2-console` |
| DB username | `sa` |
| DB password | *(empty)* |
| JWT expiration | `3600000` ms (1 hour) |

## Testing Optimistic Locking (Concurrency)

We have (`test_concurrency.py`) in the root directory to showcase database transactions and the **Optimistic Locking** mechanism in action. It spins up two parallel threads that attempt to claim the exact same `Task` simultaneously to prove that the API properly rejects concurrent modifications.

**To run the test:**

1. Stop your Spring Boot application if it's currently running.
2. Start (or restart) the application so the database seeds a fresh `.tenant_ids.txt` file.
3. Open a separate terminal and run:

   ```bash
   python test_concurrency.py
   ```

4. Observe the terminal output: One thread will gracefully succeed with a `202 Accepted`, while the other will trigger an Optimistic Lock exception and should roll-back in the database.
