package training.iqgateway.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import training.iqgateway.entities.BookingPayment;
import training.iqgateway.entities.PaymentStatus;

@Repository
public interface PaymentRepository extends MongoRepository<BookingPayment, String> {
    Optional<BookingPayment> findByAppointmentId(String appointmentId);
    Optional<BookingPayment> findByRazorpayOrderId(String razorpayOrderId);
    Optional<BookingPayment> findByRazorpayPaymentId(String razorpayPaymentId);
    List<BookingPayment> findByPatientIdAndStatus(String patientId, PaymentStatus status);
    List<BookingPayment> findByPatientId(String patientId);
    List<BookingPayment> findByStatus(PaymentStatus status);
}
