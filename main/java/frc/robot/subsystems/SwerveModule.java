package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;


import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SwerveModule extends SubsystemBase {

    CANcoder absoluteEncoder;
    CANSparkMax driveMotor;
    CANSparkMax turningMotor;

    RelativeEncoder relativeEncoder;

    SparkMaxPIDController pidController;

    public static final double turningGearRatio = 1.0/(150/7.0) * 2 * Math.PI;
    double offset;

    double initialAbsoluteEncoderValue;
    String name;

    public SwerveModule(int absoluteEncoderID, int driveMotorID, int turningMotorID, double offset, String name){
        this.offset = offset;
        this.name = name;
        absoluteEncoder = new CANcoder(absoluteEncoderID,"rio");
        absoluteEncoder.getConfigurator().apply(new CANcoderConfiguration());
        absoluteEncoder.getPosition().setUpdateFrequency(10);
        driveMotor = new CANSparkMax(driveMotorID, CANSparkMax.MotorType.kBrushless);
        turningMotor = new CANSparkMax(turningMotorID, CANSparkMax.MotorType.kBrushless);

        driveMotor.restoreFactoryDefaults();
        turningMotor.restoreFactoryDefaults();

        pidController = turningMotor.getPIDController();

        relativeEncoder = turningMotor.getEncoder();
        turningMotor.setInverted(true);

        relativeEncoder.setPositionConversionFactor(turningGearRatio);

        driveMotor.getEncoder().setVelocityConversionFactor(Units.inchesToMeters(2) * Math.PI * 2 * 6.75 / 420.0);

        relativeEncoder.setPosition(((absoluteEncoder.getAbsolutePosition().getValueAsDouble() * 2 * Math.PI) - offset));

        pidController.setP(0.8);





    }

    public void setAngle(double angle){
        pidController.setReference(angle, com.revrobotics.ControlType.kPosition);
    }

    public void setPower(double power){
        driveMotor.set(power);
    }



    @Override
    public void periodic(){
    //    SmartDashboard.putNumber("encoderValue",relativeEncoder.getPosition());
        SmartDashboard.putNumber(name+"_ABS",absoluteEncoder.getAbsolutePosition().getValueAsDouble() * 2 * Math.PI);

    }

    public void setState(SwerveModuleState state){
        SwerveModuleState.optimize(state, Rotation2d.fromRadians(getAngle()));
        setAngle(state.angle.getRadians());
        setPower(state.speedMetersPerSecond / 1.5);
    }
    public double getAngle(){
        return relativeEncoder.getPosition();
    }



}
