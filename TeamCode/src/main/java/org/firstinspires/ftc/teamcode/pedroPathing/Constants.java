package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {

    // XAVIER NOTE:  While tuning for Autonomous, the Pedro Pathing documentation will tell you to copy and paste over a "MecanumConstants" declaration statement.
    //   We already did that below, so just add on to it or modify it as necessary. You will still need to copy and paste certain things over, though,
    //   such as a localizerConstants declaration statement

    final static double lbPerKg = 2.205;
    final static double robotWeightInPounds = 20; // TODO: Update with your robot's actual weight
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(robotWeightInPounds /lbPerKg);

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("front_right") // TODO: Set these names to the drive motor names used in your configuration file
            .rightRearMotorName("back_right")
            .leftRearMotorName("back_left")
            .leftFrontMotorName("front_left")
            .leftFrontMotorDirection(DcMotorSimple.Direction.FORWARD) // TODO: Set these directions based on your robot's drive motors
            .leftRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightRearMotorDirection(DcMotorSimple.Direction.REVERSE);

    public static PathConstraints pathConstraints = new PathConstraints(
            0.99,
            100,
            1,
            1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .build();
    }
}