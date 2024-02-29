package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
//test
public class SwerveSubsystem extends SubsystemBase {
    double trackWidth = Units.inchesToMeters(22.5 / 2.0);
    SwerveDriveKinematics kinematics = new SwerveDriveKinematics(

            new Translation2d[]{
                    new Translation2d(trackWidth, trackWidth),
                    new Translation2d(trackWidth, -trackWidth),
                    new Translation2d(-trackWidth, trackWidth),
                    new Translation2d(-trackWidth, -trackWidth)
            }
    );

    //SwerveModule rearLeft = new SwerveModule(31, 11, 21, -2.41, "rearLeft");
    //SwerveModule rearRight = new SwerveModule(33, 13, 23, -0.51, "rearRight");
    //SwerveModule frontLeft = new SwerveModule(30, 10, 20, 1.35, "frontLeft");
    //SwerveModule frontRight = new SwerveModule(32, 12, 22, 1.42, "frontRight");

    SwerveModule frontLeft = new SwerveModule(31, 11, 21, -2.41, "frontLeft");
    SwerveModule rearLeft = new SwerveModule(33, 13, 23, -0.51, "rearLeft");
    SwerveModule frontRight = new SwerveModule(30, 10, 20, 1.35, "frontRight");
    SwerveModule rearRight = new SwerveModule(32, 12, 22, 1.42, "rearRight");

    public SwerveSubsystem() {
    }

    // basically, if input_value > 0, return 1, otherwise return 0 (even if negative)
    public double Sgn( double input_value ) {
        if (input_value > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public void drive(double x, double y, double rot) {


        // BEGIN GC MODS
        double Deadband_x;
        double Deadband_y;
        double Deadband_z;
        
        double mod_x;
        double mod_y;
        double mod_z;

        Deadband_x = 0.3;
        Deadband_y = 0.3;
        Deadband_z = 0.3;

        if( Math.abs(x) < Deadband_x ) {
            mod_x = 0;
        } else {
            mod_x = ( 1 / ( 1 - Deadband_x ) ) * ( x + ( -Sgn(x) * Deadband_x ) );
        }

        if( Math.abs(y) < Deadband_y ) {
            mod_y = 0;
        } else {
            mod_y = ( 1 / ( 1 - Deadband_y ) ) * ( y + ( -Sgn(y) * Deadband_y ) );
        }

        if( Math.abs(rot) < Deadband_z ) {
            mod_z = 0;
        } else {
            mod_z = ( 1 / ( 1 - Deadband_z ) ) * ( rot + ( -Sgn(rot) * Deadband_z ) );
        }

        SwerveModuleState[] states = kinematics.toSwerveModuleStates(new ChassisSpeeds(mod_x, mod_y, mod_z));
        //frontLeft.setState(states[0]);
        //frontRight.setState(states[1]);
        //rearLeft.setState(states[2]);
        //rearRight.setState(states[3]);

        frontLeft.setState(states[2]);
        frontRight.setState(states[0]);
        rearLeft.setState(states[3]);
        rearRight.setState(states[1]);


        // END GC MODS

        /*
        // ORIGINAL CODE
        SwerveModuleState[] states = kinematics.toSwerveModuleStates(new ChassisSpeeds(x, y, rot));
        frontLeft.setState(states[0]);
        frontRight.setState(states[1]);
        rearLeft.setState(states[2]);
        rearRight.setState(states[3]);
        */

    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        //drive(0,0,0.5); //commented out to cut down on steering changes

    }
}


