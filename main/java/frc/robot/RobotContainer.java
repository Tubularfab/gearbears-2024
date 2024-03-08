// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.SwerveModule;
import frc.robot.subsystems.SwerveSubsystem;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...

  //PWMSparkMax shooterRight = new PWMSparkMax(42);
  //PWMSparkMax shooterLeft = new PWMSparkMax(41);

  
  

  SwerveSubsystem swerveSubsystem = new SwerveSubsystem();
  private final Shooter m_shooter = new Shooter();
  private final Intake m_intake = new Intake();
  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final Joystick driver = new Joystick(0);

  // Bindings
  Trigger buttonOne = new JoystickButton(driver, 1);
  Trigger buttonNine = new JoystickButton(driver, 9);
  Trigger buttonTen = new JoystickButton(driver, 10);
  Trigger buttonEleven = new JoystickButton(driver, 11); 
  Trigger buttonTwelve = new JoystickButton(driver, 12);




  /** The container for the robot. Contains subsystems, OI devices, and commands. */
      public RobotContainer() {
          // Configure the trigger bindings

        swerveSubsystem.setDefaultCommand(new RunCommand(()-> swerveSubsystem.drive(driver.getX(), (-driver.getY()), (-driver.getZ())), swerveSubsystem));
        
        
        
        configureBindings();
      } // end RobotContainer

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private Command turnInTakeOn() {
    
    return null;
    }
    
  private void configureBindings() {
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
    //new JoystickButton(driver, 1).whileTrue(m_shooter.setShooterSpeed(0.5));
    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.

    buttonOne.whileTrue(m_shooter.getShooterCommand());
    buttonTen.whileTrue(new SequentialCommandGroup(
      m_shooter.getStartShooterCommand(),
      new WaitCommand(1.5),
      m_intake.getRunIntakeCommand()

    ).handleInterrupt(() -> {m_shooter.setShooterSpeed(0);}));
    buttonEleven.whileTrue(m_intake.getRunIntakeCommand());
    buttonNine.whileTrue(m_shooter.getSlowShootCommand());
    buttonTwelve.whileTrue(new ParallelCommandGroup(
      m_shooter.getRunReverseShooter(),
      m_intake.getRunOutakeCommand()));

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  //private void getAutonomousCommand() {
    public Command getAutonomousCommand() {
    // An example command will be run in autonomous

      // return new SequentialCommandGroup(
      //   new ParallelCommandGroup(
      //   m_shooter.getShooterCommand(),
      //   m_intake.getRunIntakeCommand()),

      //   new WaitCommand(1),

      //   new ParallelCommandGroup(m_shooter.getStopCommand(), m_intake.getStopCommand())
      // );

            return new SequentialCommandGroup(
                m_shooter.getStartShooterCommand(),
                new WaitCommand(.50),

                m_intake.getRunIntakeCommand().withTimeout(.5),
                m_shooter.getStopCommand(),
                new ParallelCommandGroup(
                  // swerveSubsystem.getDriveStraightCommand(.3).withTimeout(2.75), 
                  // m_intake.getRunIntakeCommand().withTimeout(2.75),
                  swerveSubsystem.getDriveStraightCommand(.3), 
                  m_intake.getRunIntakeCommand()
                ).withTimeout(2.75),
                swerveSubsystem.getDriveStraightCommand(-.3).withTimeout(2.75)
                // new WaitCommand(.50),
                // m_shooter.getStartShooterCommand()

            );
        
        // new swerveSubsystem.setDefaultCommand(new RunCommand(()-> swerveSubsystem.drive(driver.getX(), (-driver.getY(3)), (-driver.getZ())), swerveSubsystem));

      // pathplanner - tool to review math & trajectories

  } // end getAutonomousCommand

   
   
    
  

} // end class