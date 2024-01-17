package dev.wise.employeemanagement;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestcontainersTest extends AbstractTestcontainers {

    @Test
    void canStartPostgresDB() {
        assertThat(container.isRunning()).isTrue();
        assertThat(container.isCreated()).isTrue();
    }
}

