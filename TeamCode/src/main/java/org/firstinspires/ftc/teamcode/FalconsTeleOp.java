package org.firstinspires.ftc.teamcode;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
@Configurable
public class FalconsTeleOp extends OpMode {
    //Initialize motors, servos, sensors, imus, etc.
    DcMotorEx motorLF, motorRF, motorLB, motorRB, motorWorm, motorSlide;
    Servo servoClaw;

    double servoOn = 0.5, servoOff = 0.2;
    boolean servoOpen = false;

    // The following code will run as soon as "INIT" is pressed on the Driver Station
    @Override
    public void init() {

        // Set up drive motors
        // The names for each motor are taken from the driveConstants in the Constants file
        // TODO: Update "Constants" with the names of your drive motors from the driver station configuration file
        motorLF = (DcMotorEx) hardwareMap.dcMotor.get( Constants.driveConstants.leftFrontMotorName );
        motorLB = (DcMotorEx) hardwareMap.dcMotor.get( Constants.driveConstants.leftRearMotorName );
        motorRF = (DcMotorEx) hardwareMap.dcMotor.get( Constants.driveConstants.rightFrontMotorName );
        motorRB = (DcMotorEx) hardwareMap.dcMotor.get( Constants.driveConstants.rightRearMotorName );
        motorWorm = (DcMotorEx) hardwareMap.dcMotor.get("worm");
        motorSlide = (DcMotorEx) hardwareMap.dcMotor.get("slide");

        servoClaw = (Servo) hardwareMap.servo.get("claw");


        // Use the following line as a template for defining new servos
        //claw = (Servo) hardwareMap.servo.get("claw");

        // Reverse certain drive motors so that positive power to all motors makes the robot move forwards
        motorLF.setDirection( Constants.driveConstants.leftFrontMotorDirection );
        motorLB.setDirection( Constants.driveConstants.leftRearMotorDirection );
        motorRF.setDirection( Constants.driveConstants.rightFrontMotorDirection );
        motorRB.setDirection( Constants.driveConstants.rightRearMotorDirection );
        motorWorm.setDirection(DcMotorSimple.Direction.FORWARD);
        motorSlide.setDirection(DcMotorSimple.Direction.FORWARD);

        // This resets the encoder values when the code is initialized
        motorLF.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motorLB.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motorRF.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motorRB.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motorWorm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // This makes the wheels tense up and stay in position when it is not moving, opposite is FLOAT
        motorLF.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorLB.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorRF.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorRB.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorWorm.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorSlide.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        // This lets you look at encoder values while the OpMode is active
        // If you have a STOP_AND_RESET_ENCODER, make sure to put this below it
        motorLF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorWorm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    // This code runs repeatedly until the Stop button is pressed on the Driver Station
    // Replaces the old  while(OpModeIsActive())  loop
    @Override
    public void loop() {

        // Mecanum drive code
        double powerX = 0.0;  // Desired power for strafing           (-1 to 1)
        double powerY = 0.0;  // Desired power for forward/backward   (-1 to 1)
        double powerAng = 0.0;  // Desired power for turning          (-1 to 1)

        // Set the desired powers based on joystick inputs (-1 to 1)
        powerX = gamepad1.left_stick_x;
        powerY = -gamepad1.left_stick_y;
        powerAng = -gamepad1.right_stick_x;

        // Perform vector math to determine the desired powers for each wheel
        double powerLF = powerX + powerY - powerAng;
        double powerLB = -powerX + powerY - powerAng;
        double powerRF = -powerX + powerY + powerAng;
        double powerRB = powerX + powerY + powerAng;

        // Determine the greatest wheel power and set it to max
        double max = Math.max(1.0, Math.abs(powerLF));
        max = Math.max(max, Math.abs(powerRF));
        max = Math.max(max, Math.abs(powerLB));
        max = Math.max(max, Math.abs(powerRB));

        // Scale all power variables down to a number between 0 and 1 (so that setPower will accept them)
        powerLF /= max;
        powerLB /= max;
        powerRF /= max;
        powerRB /= max;

        motorLF.setPower(0.75*powerLF);
        motorLB.setPower(0.75*powerLB);
        motorRF.setPower(0.75*powerRF);
        motorRB.setPower(0.75*powerRB);


        // ARM LOGIC
        if (gamepad1.rightBumperWasPressed()) {
            servoOpen = !servoOpen;
        }
        if (servoOpen) {
            servoClaw.setPosition(servoOn);
        } else {
            servoClaw.setPosition(servoOff);
        }

        if (gamepad1.left_bumper) {
            motorWorm.setPower(0.5);
        } else if (gamepad1.left_trigger > 0.2) {
            motorWorm.setPower(-0.5);
        } else {
            motorWorm.setPower(0);
        }

        if (gamepad1.dpad_up) {
            motorSlide.setPower(0.5);
        } else if (gamepad1.dpad_down) {
            motorSlide.setPower(-0.5);
        } else {
            motorSlide.setPower(0);
        }


        // This type of boolean is a new addition to the FTC SDK
        // It will be true ONLY when the specified button changes state from not being pressed to being pressed
        //   Useful for toggle systems and as a replacement for the old (button && !lastButton) approach

        //if (gamepad1.rightBumperWasPressed()) { /* CODE */ }



        // If you want to print information to the Driver Station, use telemetry
        // addData() lets you give a string which is automatically followed by a ":" when printed
        //     the variable that you list after the comma will be displayed next to the label
        // update() only needs to be run once and will "push" all of the added data

        telemetry.addData("slidePos", motorSlide.getCurrentPosition());
        telemetry.addData("wormPos", motorWorm.getCurrentPosition());
        telemetry.addData("servoPos", servoClaw.getPosition());
        telemetry.update();

    }

    // Any additional methods go here

}