package cleanarchitecture.domain.common;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;


public class StringUtilsTest {

    @Test
    public void isEmpty() {
        assertThat(StringUtils.isEmpty((String) null)).isTrue();
        assertThat(StringUtils.isEmpty("")).isTrue();
        assertThat(StringUtils.isEmpty("2112")).isFalse();
        assertThat(StringUtils.isEmpty(" ")).isFalse();
    }

    @Test
    public void isEmptyAny() {
        assertThat(StringUtils.isEmpty(null, "Hi")).isTrue();
        assertThat(StringUtils.isEmpty("", "Hi")).isTrue();
        assertThat(StringUtils.isEmpty("2", "Hi")).isFalse();
        assertThat(StringUtils.isEmpty("2", "  ")).isFalse();
    }
}