package training.iqgateway.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import training.iqgateway.dto.DoctorAvailabilityResponseDto;
import training.iqgateway.entities.DoctorAvailability;
import training.iqgateway.repositories.DoctorAvailabilityRepository;

@Service
@Transactional
public class DoctorService {

    // ... existing autowired fields ...
    @Autowired
    private DoctorAvailabilityRepository availabilityRepository;
    
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


}
