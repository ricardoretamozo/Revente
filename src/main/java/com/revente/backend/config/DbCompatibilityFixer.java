package com.revente.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DbCompatibilityFixer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        // Fix for "column is of type enum but expression is of type character varying"
        // We create an IMPLICIT CAST so Postgres automatically converts varchar to the
        // enum.

        try {
            createCast("updated_at", "ticket_status"); // Just a dummy check? No, we need explicit SQL.

            // List of enums to fix
            String[] enums = { "ticket_status", "offer_status", "transaction_status", "event_status",
                    "event_category" };

            for (String enumType : enums) {
                createCastIfMissing(enumType);
            }

            System.out.println("DbCompatibilityFixer: Postgres Enum Casts verified.");
        } catch (Exception e) {
            System.err.println("DbCompatibilityFixer Warning: " + e.getMessage());
        }
    }

    private void createCastIfMissing(String enumType) {
        // Postgres strictness requires a CAST from varchar to the enum.
        // Command: CREATE CAST (character varying AS <enum>) WITH INOUT AS IMPLICIT;

        // We first check if it exists to avoid errors on restart
        String checkSql = "SELECT count(*) FROM pg_cast c JOIN pg_type s ON c.castsource = s.oid JOIN pg_type t ON c.casttarget = t.oid WHERE s.typname = 'varchar' AND t.typname = ?";

        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, enumType);

        if (count != null && count == 0) {
            String sql = String.format("CREATE CAST (character varying AS %s) WITH INOUT AS IMPLICIT", enumType);
            jdbcTemplate.execute(sql);
            System.out.println("Created implicit cast for: " + enumType);
        }
    }

    // Helper to accept text too
    private void createCast(String source, String target) {
        // Not used logic, implemented in createCastIfMissing
    }
}
