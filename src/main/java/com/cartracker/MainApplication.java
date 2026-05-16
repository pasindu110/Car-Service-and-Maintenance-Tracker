package com.cartracker;

import com.cartracker.config.DatabaseConnection;
import com.cartracker.repository.appointment.AppointmentRepository;
import com.cartracker.repository.appointment.JdbcAppointmentRepository;
import com.cartracker.repository.feedback.FeedbackRepository;
import com.cartracker.repository.feedback.JdbcFeedbackRepository;
import com.cartracker.repository.paymentcard.JdbcPaymentCardRepository;
import com.cartracker.repository.paymentcard.PaymentCardRepository;
import com.cartracker.repository.servicerecord.JdbcServiceRecordRepository;
import com.cartracker.repository.servicerecord.ServiceRecordRepository;
import com.cartracker.repository.user.JdbcUserRepository;
import com.cartracker.repository.user.UserRepository;
import com.cartracker.repository.vehicle.JdbcVehicleRepository;
import com.cartracker.repository.vehicle.VehicleRepository;
import com.cartracker.service.paymentcard.PaymentCardService;
import com.cartracker.service.paymentcard.PaymentCardServiceImpl;

import java.sql.Connection;

/**
 * ============================================================
 *  Car Service and Maintenance Tracker
 *  Entry point for the application.
 * ============================================================
 */
public class MainApplication {

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("  Car Service and Maintenance Tracker v1.0 ");
        System.out.println("===========================================");

        System.out.println("[System] Initializing Database Connection...");
        
        try {
            // 1. Test Database Connection
            Connection conn = DatabaseConnection.getInstance().getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("[System] Successfully connected to MySQL Database!");
            }

            // 2. Initialize Repositories (Using JDBC / Database)
            UserRepository userRepository = new JdbcUserRepository();
            VehicleRepository vehicleRepository = new JdbcVehicleRepository();
            AppointmentRepository appointmentRepository = new JdbcAppointmentRepository();
            ServiceRecordRepository serviceRecordRepository = new JdbcServiceRecordRepository();
            FeedbackRepository feedbackRepository = new JdbcFeedbackRepository();
            
            // Note for group members: 
            // BillingRepository and its JDBC implementation need to be created by the member assigned to Billing.

            System.out.println("[System] All Database Repositories initialized successfully.");

            // 3. Initialize Repositories – PaymentCard (JDBC / MySQL)
            PaymentCardRepository paymentCardRepository = new JdbcPaymentCardRepository();

            // 4. Initialize Services
            com.cartracker.service.user.UserService userService = new com.cartracker.service.user.UserServiceImpl(userRepository);
            PaymentCardService paymentCardService = new PaymentCardServiceImpl(paymentCardRepository);

            // 5. Start Web Server
            WebServer webServer = new WebServer(userService, paymentCardService);
            webServer.start();

            System.out.println("\n[System] Application is ready! Open http://localhost:8080 in your browser.");

        } catch (Exception e) {
            System.err.println("[System] Failed to start application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
