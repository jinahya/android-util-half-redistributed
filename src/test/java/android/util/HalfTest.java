package android.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class HalfTest {

    @Test
    void testToFloat() {
        {
            final short s = (short) 0b0_00000_0000000001;
            final float f = Half.toFloat(s);
            log.debug("{} -> {}", s, f);
        }
        {
            final short s = (short) 0b0_00000_1111111111;
            final float f = Half.toFloat(s);
            log.debug("{} -> {}", s, f);
        }
        {
            final short s = (short) 0b0_00001_0000000000;
            final float f = Half.toFloat(s);
            log.debug("{} -> {}", s, f);
        }
        {
            final short s = (short) 0b0_11110_1111111111;
            final float f = Half.toFloat(s);
            log.debug("{} -> {}", s, f);
        }
        {
            final short s = (short) 0b0_01110_1111111111;
            final float f = Half.toFloat(s);
            log.debug("{} -> {}", s, f);
        }
        {
            final short s = (short) 0b0_01111_0000000000;
            final float f = Half.toFloat(s);
            log.debug("{} -> {}", s, f);
        }
        {
            final short s = (short) 0b0_01111_0000000001;
            final float f = Half.toFloat(s);
            log.debug("{} -> {}", s, f);
        }
        {
            final short s = (short) 0b0_01101_0101010101;
            final float f = Half.toFloat(s);
            log.debug("{} -> {}", s, f);
        }
        {
            final short s = (short) 0b1_10000_0000000000;
            final float f = Half.toFloat(s);
            log.debug("{} -> {}", s, f);
            assertThat(f).isEqualTo(-2.0f);
        }
        {
            final short s = (short) 0b0_00000_0000000000;
            final float f = Half.toFloat(s);
            log.debug("{} -> {}", s, f);
            assertThat(f).isEqualTo(+0.0f);
        }
        {
            final short s = (short) 0b1_00000_0000000000;
            final float f = Half.toFloat(s);
            log.debug("{} -> {}", s, f);
            assertThat(f).isEqualTo(-0.0f);
        }
        {
            final short s = (short) 0b0_11111_0000000000;
            final float f = Half.toFloat(s);
            log.debug("{} -> {}", s, f);
            assertThat(f).isEqualTo(Float.POSITIVE_INFINITY);
        }
        {
            final short s = (short) 0b1_11111_0000000000;
            final float f = Half.toFloat(s);
            log.debug("{} -> {}", s, f);
            assertThat(f).isEqualTo(Float.NEGATIVE_INFINITY);
        }
    }

    @Test
    void toZero() {
        {
            final short expected = (short) 0b0_00000_0000000000;
            final short actual = Half.toHalf(Half.toFloat(expected));
            assertThat(actual)
                    .isEqualTo(expected);
        }
        {
            final short expected = (short) 0b1_00000_0000000000;
            final short actual = Half.toHalf(Half.toFloat(expected));
            assertThat(actual)
                    .isEqualTo(expected);
        }
    }

    @Test
    void testSubnormal() {
        {
            final short expected = (short) (0b0_00000_0000000000 | current().nextInt(1, 0b1_0000000000));
            final short actual = Half.toHalf(Half.toFloat(expected));
            assertThat(actual)
                    .isEqualTo(expected);
        }
        {
            final short expected = (short) (0b1_00000_0000000000 | current().nextInt(1, 0b1_0000000000));
            final short actual = Half.toHalf(Half.toFloat(expected));
            assertThat(actual)
                    .isEqualTo(expected);
        }
    }

    @Test
    void testNormalizedValue() {
        final int sign = current().nextInt(2);
        final int exponent = current().nextInt(1, 0b1_00000);
        final int fraction = current().nextInt(0b1_0000000000);
        final short expected = (short) ((sign << 15) | (exponent << 10) | fraction);
        final short actual = Half.toHalf(Half.toFloat(expected));
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void testInfinity() {
        final int sign = current().nextInt(2);
        final int exponent = 0b11111;
        final int fraction = 0b0000000000;
        final short expected = (short) ((sign << 15) | (exponent << 10) | fraction);
        final float f = Half.toFloat(expected);
        assertThat(f).isEqualTo(sign == 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY);
        final short actual = Half.toHalf(f);
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void testNan() {
        final int sign = current().nextInt(2);
        final int exponent = 0b11111;
        final int fraction = current().nextInt(1, 0b1_0000000000);
        final short expected = (short) ((sign << 15) | (exponent << 10) | fraction);
        final float f = Half.toFloat(expected);
        assertThat(f).isNaN();
        final short actual = Half.toHalf(f);
    }
}