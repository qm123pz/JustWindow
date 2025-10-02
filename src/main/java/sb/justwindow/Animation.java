package sb.justwindow;

import static java.lang.Math.*;

public class Animation {
    public static double easeInSine(double x) {
        return 1 - cos((x * PI) / 2);
    }

    public static double easeOutSine(double x) {
        return sin((x * PI) / 2);
    }

    public static double easeInOutSine(double x) {
        return -(cos(PI * x) - 1) / 2;
    }

    public static double easeInQuad(double x) {
        return x * x;
    }

    public static double easeOutQuad(double x) {
        return 1 - (1 - x) * (1 - x);
    }

    public static double easeInOutQuad(double x) {
        return x < 0.5 ? 2 * x * x : 1 - pow(-2 * x + 2, 2) / 2;
    }

    public static double easeInCubic(double x) {
        return x * x * x;
    }

    public static double easeOutCubic(double x) {
        return 1 - pow(1 - x, 3);
    }

    public static double easeInOutCubic(double x) {
        return x < 0.5 ? 4 * x * x * x : 1 - pow(-2 * x + 2, 3) / 2;
    }

    public static double easeInQuart(double x) {
        return x * x * x * x;
    }

    public static double easeOutQuart(double x) {
        return 1 - pow(1 - x, 4);
    }

    public static double easeInOutQuart(double x) {
        return x < 0.5 ? 8 * x * x * x * x : 1 - pow(-2 * x + 2, 4) / 2;
    }

    public static double easeInQuint(double x) {
        return x * x * x * x * x;
    }

    public static double easeOutQuint(double x) {
        return 1 - pow(1 - x, 5);
    }

    public static double easeInOutQuint(double x) {
        return x < 0.5 ? 16 * x * x * x * x * x : 1 - pow(-2 * x + 2, 5) / 2;
    }

    public static double easeInExpo(double x) {
        return x == 0.0 ? 0.0 : pow(2, 10 * x - 10);
    }

    public static double easeOutExpo(double x) {
        return x == 1.0 ? 1.0 : 1 - pow(2, -10 * x);
    }

    public static double easeInOutExpo(double x) {
        if (x == 0.0) {
            return 0.0;
        } else if (x == 1.0) {
            return 1.0;
        } else if (x < 0.5) {
            return pow(2, 20 * x - 10) / 2;
        } else {
            return (2 - pow(2, -20 * x + 10)) / 2;
        }
    }

    public static double easeInCirc(double x) {
        return 1 - sqrt(1 - pow(x, 2));
    }

    public static double easeOutCirc(double x) {
        return sqrt(1 - pow(x - 1, 2));
    }

    public static double easeInOutCirc(double x) {
        return x < 0.5 ? (1 - sqrt(1 - pow(2 * x, 2))) / 2 : (sqrt(1 - pow(-2 * x + 2, 2)) + 1) / 2;
    }

    public static double easeInBack(double x) {
        double c1 = 1.70158;
        double c3 = c1 + 1;

        return c3 * x * x * x - c1 * x * x;
    }

    public static double easeOutBack(double x) {
        double c1 = 1.70158;
        double c3 = c1 + 1;

        return 1 + c3 * pow(x - 1, 3) + c1 * pow(x - 1, 2);
    }

    public static double easeInOutBack(double x) {
        double c1 = 1.70158;
        double c2 = c1 * 1.525;

        return x < 0.5 ? (pow(2 * x, 2) * ((c2 + 1) * 2 * x - c2)) / 2 : (pow(2 * x - 2, 2) * ((c2 + 1) * (x * 2 - 2) + c2) + 2) / 2;
    }

    public static double easeInElastic(double x) {
        double c4 = (2 * PI) / 3;

        if (x == 0.0) {
            return 0.0;
        } else if (x == 1.0) {
            return 1.0;
        } else {
            return -pow(2, 10 * x - 10) * sin((x * 10 - 10.75) * c4);
        }
    }

    public static double easeOutElastic(double x) {
        double c4 = (2 * PI) / 3;

        if (x == 0.0) {
            return 0.0;
        } else if (x == 1.0) {
            return 1.0;
        } else {
            return pow(2, -10 * x) * sin((x * 10 - 0.75) * c4) + 1;
        }
    }

    public static double easeInOutElastic(double x) {
        double c5 = (2 * PI) / 4.5;

        if (x == 0.0) {
            return 0.0;
        } else if (x == 1.0) {
            return 1.0;
        } else if (x < 0.5) {
            return -(pow(2, 20 * x - 10) * sin((20 * x - 11.125) * c5)) / 2;
        } else {
            return (pow(2, -20 * x + 10) * sin((20 * x - 11.125) * c5)) / 2 + 1;
        }
    }

    public static double easeInBounce(double x) {
        return 1 - easeOutBounce(1 - x);
    }

    public static double easeOutBounce(double animeX) {
        double x = animeX;
        double n1 = 7.5625;
        double d1 = 2.75;

        if (x < 1 / d1) {
            return n1 * x * x;
        } else if (x < 2 / d1) {
            x -= 1.5;
            return n1 * (x / d1) * x + 0.75;
        } else if (x < 2.5 / d1) {
            x -= 2.25;
            return n1 * (x / d1) * x + 0.9375;
        } else {
            x -= 2.625;
            return n1 * (x / d1) * x + 0.984375;
        }
    }

    public static double easeInOutBounce(double x) {
        return x < 0.5 ? (1 - easeOutBounce(1 - 2 * x)) / 2 : (1 + easeOutBounce(2 * x - 1)) / 2;
    }
}