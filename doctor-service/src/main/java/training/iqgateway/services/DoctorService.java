package training.iqgateway.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import training.iqgateway.dto.DoctorAvailabilityResponseDto;
import training.iqgateway.entities.Doctor;
import training.iqgateway.entities.DoctorAvailability;
import training.iqgateway.entities.User;
import training.iqgateway.repositories.DoctorAvailabilityRepository;
import training.iqgateway.repositories.DoctorRepository;

@Service
@Transactional
public class DoctorService {

  

    // ... existing autowired fields ...
    @Autowired
    private DoctorAvailabilityRepository availabilityRepository;
    	
    @Autowired
    private DoctorRepository doctorRepository;
    
    // ... existing methods ...

    // NEW METHOD - Get all doctor availabilities
    public List<DoctorAvailabilityResponseDto> getAllDoctorAvailabilities() {
        List<DoctorAvailability> availabilities = availabilityRepository.findAllAvailabilities();
        
        return availabilities.stream()
                .map(this::convertToAvailabilityResponseDto)
                .collect(Collectors.toList());
    }

    // Get only available doctor availabilities
    public List<DoctorAvailabilityResponseDto> getAllAvailableDoctorAvailabilities() {
        List<DoctorAvailability> availabilities = availabilityRepository.findByIsAvailableTrue();
        
        return availabilities.stream()
                .filter(avail -> avail.getDoctor().isActive()) // Only active doctors
                .map(this::convertToAvailabilityResponseDto)
                .collect(Collectors.toList());
    }

    // Get availabilities by day of week
    public List<DoctorAvailabilityResponseDto> getDoctorAvailabilitiesByDay(String dayOfWeek) {
        List<DoctorAvailability> availabilities = availabilityRepository.findByDayOfWeekAndIsAvailableTrue(dayOfWeek);
        
        System.out.println("Found " + availabilities.size() + " availabilities for day: " + dayOfWeek);
        return availabilities.stream()
                .filter(avail -> avail.getDoctor().isActive()) // Only active doctors
                .map(this::convertToAvailabilityResponseDto)
                .collect(Collectors.toList());
    }

    // Helper method to convert entity to response DTO
    private DoctorAvailabilityResponseDto convertToAvailabilityResponseDto(DoctorAvailability availability) {
        DoctorAvailabilityResponseDto dto = new DoctorAvailabilityResponseDto();
        
        dto.setId(availability.getId());
        dto.setDoctorId(availability.getDoctor().getId());
        dto.setDoctorName(availability.getDoctor().getUser().getUsername());
        dto.setDoctorEmail(availability.getDoctor().getUser().getEmail());
        dto.setDoctorSpecializations(availability.getDoctor().getSpecializations());
        dto.setDayOfWeek(availability.getDayOfWeek());
        dto.setStartTime(availability.getStartTime());
        dto.setEndTime(availability.getEndTime());
        dto.setAvailable(availability.isAvailable());
        dto.setAppointmentDuration(availability.getAppointmentDuration());
        dto.setDoctorActive(availability.getDoctor().isActive());
        
        return dto;
    }
    
    public Doctor getByUserId(String userId) {
		return doctorRepository.findByUser_Id(userId)
				.orElseThrow(() -> new RuntimeException("Doctor not found for user ID: " + userId));
    }
    
    public Doctor updateDoctor(String id, Doctor updatedDoctor) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(id);
        if (optionalDoctor.isPresent()) {
            Doctor existingDoctor = optionalDoctor.get();
            
            // Update simple fields
            existingDoctor.setActive(updatedDoctor.isActive());
            existingDoctor.setProfilePhoto(updatedDoctor.getProfilePhoto());
            existingDoctor.setSpecializations(updatedDoctor.getSpecializations());
            
            // Optionally update user fields if present (ensure non-null to avoid overwriting with null)
            if (updatedDoctor.getUser() != null) {
                User existingUser = existingDoctor.getUser();
                User updatedUser = updatedDoctor.getUser();

                if (updatedUser.getId() != null) {
                    // Update relevant user fields safely
                    if (updatedUser.getUsername() != null) {
                        existingUser.setUsername(updatedUser.getUsername());
                    }
                    if (updatedUser.getEmail() != null) {
                        existingUser.setEmail(updatedUser.getEmail());
                    }
                    // You can add password or other fields update here if needed,
                    // but be cautious with sensitive fields.
                }
            }

            return doctorRepository.save(existingDoctor);
        }
        return null;
    }
    
    public List<Doctor> getAllDoctors() {
		return doctorRepository.findAll();
	}

}
