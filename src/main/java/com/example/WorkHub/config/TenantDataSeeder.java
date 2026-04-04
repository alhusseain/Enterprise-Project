package com.example.WorkHub.config;

import com.example.WorkHub.model.Tenant;
import com.example.WorkHub.repository.TenantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Profile("!test")
public class TenantDataSeeder implements CommandLineRunner {

    private final TenantRepository tenantRepository;

    public TenantDataSeeder(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        List<TenantSeed> seeds = List.of(
                new TenantSeed("Oscorp Industires", "BASIC"),
                new TenantSeed("Globex", "PRO"),
                new TenantSeed("Initech", "ENTERPRISE")
        );

        Set<String> existingNames = tenantRepository.findAll()
                .stream()
                .map(Tenant::getName)
                .collect(Collectors.toSet());

        List<Tenant> missingTenants = seeds.stream()
                .filter(seed -> !existingNames.contains(seed.name()))
                .map(seed -> {
                    Tenant tenant = new Tenant();
                    tenant.setName(seed.name());
                    tenant.setPlan(seed.plan());
                    return tenant;
                })
                .toList();

        if (!missingTenants.isEmpty()) {
            tenantRepository.saveAll(missingTenants);
        }
    }

    private record TenantSeed(String name, String plan) {
    }
}
