package cleanarchitecture.domain.common.ex;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;



public class ApplicationExceptionTest {

    @Test
    public void testExceptionStructure() {
        ApplicationException exception = new ApplicationException("msj", "code001");
        assertThat(exception).hasMessage("msj");
        assertThat(exception.getCode()).isEqualTo("code001");
    }
}