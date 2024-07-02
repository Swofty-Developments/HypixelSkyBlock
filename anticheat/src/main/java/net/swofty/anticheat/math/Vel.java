package net.swofty.anticheat.math;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoubleUnaryOperator;

@Getter
@Setter
public final class Vel implements Point {
    public static final double EPSILON = 0.000001;

    private double x;
    private double y;
    private double z;

    public Vel(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Creates a new point with coordinated depending on {@code this}.
     *
     * @param operator the operator
     * @return the created point
     */
    @Contract(pure = true)
    public @NotNull Vel apply(@NotNull Operator operator) {
        return operator.apply(x, y, z);
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

    @Contract(pure = true)
    public @NotNull Vel withX(@NotNull DoubleUnaryOperator operator) {
        return new Vel(operator.applyAsDouble(x), y, z);
    }

    @Contract(pure = true)
    public @NotNull Vel withX(double x) {
        return new Vel(x, y, z);
    }

    public static @NotNull Vel fromPoint(@NotNull Point point) {
        if (point instanceof Vel vec) return vec;
        return new Vel(point.x(), point.y(), point.z());
    }

    @Contract(pure = true)
    public @NotNull Vel withY(@NotNull DoubleUnaryOperator operator) {
        return new Vel(x, operator.applyAsDouble(y), z);
    }

    @Contract(pure = true)
    public @NotNull Vel withY(double y) {
        return new Vel(x, y, z);
    }

    @Contract(pure = true)
    public @NotNull Vel withZ(@NotNull DoubleUnaryOperator operator) {
        return new Vel(x, y, operator.applyAsDouble(z));
    }

    @Contract(pure = true)
    public @NotNull Vel withZ(double z) {
        return new Vel(x, y, z);
    }

    public @NotNull Vel add(double x, double y, double z) {
        return new Vel(this.x + x, this.y + y, this.z + z);
    }

    @Override
    public @NotNull Point add(@NotNull Point point) {
        return null;
    }

    public @NotNull Vel add(@NotNull Pos point) {
        return add(point.x(), point.y(), point.z());
    }

    public @NotNull Vel add(double value) {
        return add(value, value, value);
    }

    public @NotNull Vel sub(double x, double y, double z) {
        return new Vel(this.x - x, this.y - y, this.z - z);
    }

    public @NotNull Vel sub(@NotNull Point point) {
        return sub(point.x(), point.y(), point.z());
    }

    public @NotNull Vel sub(double value) {
        return sub(value, value, value);
    }

    public @NotNull Vel mul(double x, double y, double z) {
        return new Vel(this.x * x, this.y * y, this.z * z);
    }

    public @NotNull Vel mul(@NotNull Point point) {
        return mul(point.x(), point.y(), point.z());
    }

    public @NotNull Vel mul(double value) {
        return mul(value, value, value);
    }

    public @NotNull Vel div(double x, double y, double z) {
        return new Vel(this.x / x, this.y / y, this.z / z);
    }

    @Override
    public @NotNull Point div(@NotNull Point point) {
        return null;
    }

    public @NotNull Vel div(double value) {
        return div(value, value, value);
    }

    @Contract(pure = true)
    public @NotNull Vel neg() {
        return new Vel(-x, -y, -z);
    }

    public static @NotNull Vel fromPos(@NotNull Pos point) {
        return new Vel(point.x(), point.y(), point.z());
    }

    @Contract(pure = true)
    public @NotNull Vel abs() {
        return new Vel(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    @Contract(pure = true)
    public @NotNull Vel min(double value) {
        return new Vel(Math.min(x, value), Math.min(y, value), Math.min(z, value));
    }

    @Contract(pure = true)
    public @NotNull Vel max(double value) {
        return new Vel(Math.max(x, value), Math.max(y, value), Math.max(z, value));
    }

    @Contract(pure = true)
    public @NotNull Pos asPosition() {
        return new Pos(x, y, z);
    }

    /**
     * Gets the magnitude of the Veltor squared.
     *
     * @return the magnitude
     */
    @Contract(pure = true)
    public double lengthSquared() {
        return MathUtils.square(x) + MathUtils.square(y) + MathUtils.square(z);
    }

    /**
     * Gets the magnitude of the Veltor, defined as sqrt(x^2+y^2+z^2). The
     * value of this method is not cached and uses a costly square-root
     * function, so do not repeatedly call this method to get the Veltor's
     * magnitude. NaN will be returned if the inner result of the sqrt()
     * function overflows, which will be caused if the length is too long.
     *
     * @return the magnitude
     */
    @Contract(pure = true)
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Converts this Veltor to a unit Veltor (a Veltor with length of 1).
     *
     * @return the same Veltor
     */
    @Contract(pure = true)
    public @NotNull Vel normalize() {
        final double length = length();
        return new Vel(x / length, y / length, z / length);
    }

    /**
     * Returns if a Veltor is normalized
     *
     * @return whether the Veltor is normalised
     */
    public boolean isNormalized() {
        return Math.abs(lengthSquared() - 1) < EPSILON;
    }

    /**
     * Gets the angle between this Veltor and another in radians.
     *
     * @param Vel the other Veltor
     * @return angle in radians
     */
    @Contract(pure = true)
    public double angle(@NotNull Vel Vel) {
        final double dot = MathUtils.clamp(dot(Vel) / (length() * Vel.length()), -1.0, 1.0);
        return Math.acos(dot);
    }

    /**
     * Calculates the dot product of this Veltor with another. The dot product
     * is defined as x1*x2+y1*y2+z1*z2. The returned value is a scalar.
     *
     * @param Vel the other Veltor
     * @return dot product
     */
    @Contract(pure = true)
    public double dot(@NotNull Vel Vel) {
        return x * Vel.x + y * Vel.y + z * Vel.z;
    }

    /**
     * Calculates the cross product of this Veltor with another. The cross
     * product is defined as:
     * <ul>
     * <li>x = y1 * z2 - y2 * z1
     * <li>y = z1 * x2 - z2 * x1
     * <li>z = x1 * y2 - x2 * y1
     * </ul>
     *
     * @param o the other Veltor
     * @return the same Veltor
     */
    @Contract(pure = true)
    public @NotNull Vel cross(@NotNull Vel o) {
        return new Vel(y * o.z - o.y * z,
                z * o.x - o.z * x,
                x * o.y - o.x * y);
    }

    /**
     * Rotates the Veltor around the x-axis.
     * <p>
     * This piece of math is based on the standard rotation matrix for Veltors
     * in three-dimensional space. This matrix can be found here:
     * <a href="https://en.wikipedia.org/wiki/Rotation_matrix#Basic_rotations">Rotation
     * Matrix</a>.
     *
     * @param angle the angle to rotate the Veltor about. This angle is passed
     *              in radians
     * @return a new, rotated Veltor
     */
    @Contract(pure = true)
    public @NotNull Vel rotateAroundX(double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);

        double newY = angleCos * y - angleSin * z;
        double newZ = angleSin * y + angleCos * z;
        return new Vel(x, newY, newZ);
    }

    /**
     * Rotates the Veltor around the y-axis.
     * <p>
     * This piece of math is based on the standard rotation matrix for Veltors
     * in three-dimensional space. This matrix can be found here:
     * <a href="https://en.wikipedia.org/wiki/Rotation_matrix#Basic_rotations">Rotation
     * Matrix</a>.
     *
     * @param angle the angle to rotate the Veltor about. This angle is passed
     *              in radians
     * @return a new, rotated Veltor
     */
    @Contract(pure = true)
    public @NotNull Vel rotateAroundY(double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);

        double newX = angleCos * x + angleSin * z;
        double newZ = -angleSin * x + angleCos * z;
        return new Vel(newX, y, newZ);
    }

    /**
     * Rotates the Veltor around the z axis
     * <p>
     * This piece of math is based on the standard rotation matrix for Veltors
     * in three-dimensional space. This matrix can be found here:
     * <a href="https://en.wikipedia.org/wiki/Rotation_matrix#Basic_rotations">Rotation
     * Matrix</a>.
     *
     * @param angle the angle to rotate the Veltor about. This angle is passed
     *              in radians
     * @return a new, rotated Veltor
     */
    @Contract(pure = true)
    public @NotNull Vel rotateAroundZ(double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);

        double newX = angleCos * x - angleSin * y;
        double newY = angleSin * x + angleCos * y;
        return new Vel(newX, newY, z);
    }

    @Contract(pure = true)
    public @NotNull Vel rotate(double angleX, double angleY, double angleZ) {
        return rotateAroundX(angleX).rotateAroundY(angleY).rotateAroundZ(angleZ);
    }

    @Contract(pure = true)
    public @NotNull Vel rotateFromView(float yawDegrees, float pitchDegrees) {
        double yaw = Math.toRadians(-1 * (yawDegrees + 90));
        double pitch = Math.toRadians(-pitchDegrees);

        double cosYaw = Math.cos(yaw);
        double cosPitch = Math.cos(pitch);
        double sinYaw = Math.sin(yaw);
        double sinPitch = Math.sin(pitch);

        double initialX, initialY, initialZ;
        double x, y, z;

        // Z_Axis rotation (Pitch)
        initialX = x();
        initialY = y();
        x = initialX * cosPitch - initialY * sinPitch;
        y = initialX * sinPitch + initialY * cosPitch;

        // Y_Axis rotation (Yaw)
        initialZ = z();
        initialX = x;
        z = initialZ * cosYaw - initialX * sinYaw;
        x = initialZ * sinYaw + initialX * cosYaw;

        return new Vel(x, y, z);
    }

    @Contract(pure = true)
    public @NotNull Vel rotateFromView(@NotNull Pos pos) {
        return rotateFromView(pos.yaw(), pos.pitch());
    }

    /**
     * Rotates the Veltor around a given arbitrary axis in 3 dimensional space.
     *
     * <p>
     * Rotation will follow the general Right-Hand-Rule, which means rotation
     * will be counterclockwise when the axis is pointing towards the observer.
     * <p>
     * This method will always make sure the provided axis is a unit Veltor, to
     * not modify the length of the Veltor when rotating. If you are experienced
     * with the scaling of a non-unit axis Veltor, you can use
     * {@link Vel#rotateAroundNonUnitAxis(Vel, double)}.
     *
     * @param axis  the axis to rotate the Veltor around. If the passed Veltor is
     *              not of length 1, it gets copied and normalized before using it for the
     *              rotation. Please use {@link Vel#normalize()} on the instance before
     *              passing it to this method
     * @param angle the angle to rotate the Veltor around the axis
     * @return a new Veltor
     */
    @Contract(pure = true)
    public @NotNull Vel rotateAroundAxis(@NotNull Vel axis, double angle) throws IllegalArgumentException {
        return rotateAroundNonUnitAxis(axis.isNormalized() ? axis : axis.normalize(), angle);
    }

    /**
     * Rotates the Veltor around a given arbitrary axis in 3 dimensional space.
     *
     * <p>
     * Rotation will follow the general Right-Hand-Rule, which means rotation
     * will be counterclockwise when the axis is pointing towards the observer.
     * <p>
     * Note that the Veltor length will change accordingly to the axis Veltor
     * length. If the provided axis is not a unit Veltor, the rotated Veltor
     * will not have its previous length. The scaled length of the resulting
     * Veltor will be related to the axis Veltor. If you are not perfectly sure
     * about the scaling of the Veltor, use
     * {@link Vel#rotateAroundAxis(Vel, double)}
     *
     * @param axis  the axis to rotate the Veltor around.
     * @param angle the angle to rotate the Veltor around the axis
     * @return a new Veltor
     */
    @Contract(pure = true)
    public @NotNull Vel rotateAroundNonUnitAxis(@NotNull Vel axis, double angle) throws IllegalArgumentException {
        double x = x(), y = y(), z = z();
        double x2 = axis.x(), y2 = axis.y(), z2 = axis.z();
        double cosTheta = Math.cos(angle);
        double sinTheta = Math.sin(angle);
        double dotProduct = this.dot(axis);

        double newX = x2 * dotProduct * (1d - cosTheta)
                + x * cosTheta
                + (-z2 * y + y2 * z) * sinTheta;
        double newY = y2 * dotProduct * (1d - cosTheta)
                + y * cosTheta
                + (z2 * x - x2 * z) * sinTheta;
        double newZ = z2 * dotProduct * (1d - cosTheta)
                + z * cosTheta
                + (-y2 * x + x2 * y) * sinTheta;

        return new Vel(newX, newY, newZ);
    }

    /**
     * Calculates a linear interpolation between this Veltor with another
     * Veltor.
     *
     * @param Vel   the other Veltor
     * @param alpha The alpha value, must be between 0.0 and 1.0
     * @return Linear interpolated Veltor
     */
    @Contract(pure = true)
    public @NotNull Vel lerp(@NotNull Vel Vel, double alpha) {
        return new Vel(x + (alpha * (Vel.x - x)),
                y + (alpha * (Vel.y - y)),
                z + (alpha * (Vel.z - z)));
    }

    @Override
    public String toString() {
        return "Vel{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    @Contract(pure = true)
    public @NotNull Vel interpolate(@NotNull Vel target, double alpha, @NotNull Interpolation interpolation) {
        return lerp(target, interpolation.apply(alpha));
    }

    @FunctionalInterface
    public interface Operator {
        /**
         * Checks each axis' value, if it's below {@code Vel#EPSILON} then it gets replaced with {@code 0}
         */
        Operator EPSILON = (x, y, z) -> new Vel(
                Math.abs(x) < Vel.EPSILON ? 0 : x,
                Math.abs(y) < Vel.EPSILON ? 0 : y,
                Math.abs(z) < Vel.EPSILON ? 0 : z
        );

        Operator FLOOR = (x, y, z) -> new Vel(
                Math.floor(x),
                Math.floor(y),
                Math.floor(z)
        );

        Operator SIGNUM = (x, y, z) -> new Vel(
                Math.signum(x),
                Math.signum(y),
                Math.signum(z)
        );

        @NotNull
        Vel apply(double x, double y, double z);
    }

    @FunctionalInterface
    public interface Interpolation {
        Interpolation LINEAR = a -> a;
        Interpolation SMOOTH = a -> a * a * (3 - 2 * a);

        double apply(double a);
    }
}
