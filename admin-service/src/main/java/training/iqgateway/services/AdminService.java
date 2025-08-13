package training.iqgateway.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import training.iqgateway.dtos.DoctorAvailabilityDto;
import training.iqgateway.dtos.DoctorDto;
import training.iqgateway.dtos.DoctorRegistrationDto;
import training.iqgateway.dtos.PatientRegistrationDTO;
import training.iqgateway.dtos.UserDto;
import training.iqgateway.entities.Admin;
import training.iqgateway.entities.Doctor;
import training.iqgateway.entities.DoctorAvailability;
import training.iqgateway.entities.Patient;
import training.iqgateway.entities.User;
import training.iqgateway.repositories.AdminRepository;
import training.iqgateway.repositories.DoctorAvailabilityRepository;
import training.iqgateway.repositories.DoctorRepository;
import training.iqgateway.repositories.PatientRepository;
import training.iqgateway.repositories.UserRepository;


@Service
@Transactional
public class AdminService {

	@Autowired
	private  UserRepository userRepository;
	@Autowired
	private PatientRepository patientRepository;
	@Autowired
	private DoctorRepository doctorRepository;
	@Autowired
    private DoctorAvailabilityRepository availabilityRepository;
	@Autowired
	private AdminRepository adminRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public DoctorDto registerDoctor(DoctorRegistrationDto registrationDto) {
        
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new RuntimeException("User with email already exists");
        }
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new RuntimeException("User with username already exists");
        }


        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRole(registrationDto.getRole());
        user = userRepository.save(user);

        // Create and save Doctor
        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setActive(registrationDto.isActive());
        doctor.setSpecializations(registrationDto.getSpecializations());
        
        // Convert base64 to byte array if provided
        if (registrationDto.getProfilePictureBase64() != null) {
            doctor.setProfilePhoto(registrationDto.getProfilePictureBase64());
        }
        
        doctor = doctorRepository.save(doctor);

        // Create and save Availabilities
        List<DoctorAvailability> availabilities = new ArrayList<>();
        if (registrationDto.getAvailabilities() != null) {
            for (DoctorAvailabilityDto availDto : registrationDto.getAvailabilities()) {
                DoctorAvailability availability = new DoctorAvailability();
                availability.setDoctor(doctor);
                availability.setDayOfWeek(availDto.getDayOfWeek());
                availability.setStartTime(availDto.getStartTime());
                availability.setEndTime(availDto.getEndTime());
                availability.setAvailable(availDto.isAvailable());
                availability.setAppointmentDuration(availDto.getAppointmentDuration());
                availabilities.add(availabilityRepository.save(availability));
            }
        }

        return convertToDto(doctor, availabilities);
    }

    public List<DoctorDto> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream()
                .map(doctor -> {
                    List<DoctorAvailability> availabilities = availabilityRepository.findByDoctorId(doctor.getId());
                    return convertToDto(doctor, availabilities);
                })
                .collect(Collectors.toList());
    }

    public List<DoctorDto> getActiveDoctors() {
        List<Doctor> doctors = doctorRepository.findByActiveTrue();
        return doctors.stream()
                .map(doctor -> {
                    List<DoctorAvailability> availabilities = availabilityRepository.findByDoctorId(doctor.getId());
                    return convertToDto(doctor, availabilities);
                })
                .collect(Collectors.toList());
    }

    public Optional<DoctorDto> getDoctorById(String id) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (doctor.isPresent()) {
            List<DoctorAvailability> availabilities = availabilityRepository.findByDoctorId(id);
            return Optional.of(convertToDto(doctor.get(), availabilities));
        }
        return Optional.empty();
    }

    public List<DoctorDto> getDoctorsBySpecialization(String specialization) {
        List<Doctor> doctors = doctorRepository.findByActiveTrueAndSpecializationsContaining(specialization);
        return doctors.stream()
                .map(doctor -> {
                    List<DoctorAvailability> availabilities = availabilityRepository.findByDoctorId(doctor.getId());
                    return convertToDto(doctor, availabilities);
                })
                .collect(Collectors.toList());
    }

    public DoctorDto updateDoctor(String id, DoctorDto doctorDto) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Update doctor details
        doctor.setActive(doctorDto.isActive());
        doctor.setSpecializations(doctorDto.getSpecializations());
        
        if (doctorDto.getProfilePictureBase64() != null) {
            doctor.setProfilePhoto(doctorDto.getProfilePictureBase64());
        }

        doctor = doctorRepository.save(doctor);

        // Update availabilities
        availabilityRepository.deleteByDoctorId(id);
        List<DoctorAvailability> newAvailabilities = new ArrayList<>();
        
        if (doctorDto.getAvailabilities() != null) {
            for (DoctorAvailabilityDto availDto : doctorDto.getAvailabilities()) {
                DoctorAvailability availability = new DoctorAvailability();
                availability.setDoctor(doctor);
                availability.setDayOfWeek(availDto.getDayOfWeek());
                availability.setStartTime(availDto.getStartTime());
                availability.setEndTime(availDto.getEndTime());
                availability.setAvailable(availDto.isAvailable());
                availability.setAppointmentDuration(availDto.getAppointmentDuration());
                newAvailabilities.add(availabilityRepository.save(availability));
            }
        }

        return convertToDto(doctor, newAvailabilities);
    }

    public void deleteDoctor(String id) {
        availabilityRepository.deleteByDoctorId(id);
        doctorRepository.deleteById(id);
    }

    private DoctorDto convertToDto(Doctor doctor, List<DoctorAvailability> availabilities) {
        DoctorDto dto = new DoctorDto();
        dto.setId(doctor.getId());
        dto.setActive(doctor.isActive());
        dto.setSpecializations(doctor.getSpecializations());
        
        // Convert profile picture to base64
        if (doctor.getProfilePhoto() != null) {
            dto.setProfilePictureBase64((doctor.getProfilePhoto()));
        }

        // Convert user
        UserDto userDto = new UserDto();
        userDto.setId(doctor.getUser().getId());
        userDto.setUsername(doctor.getUser().getUsername());
        userDto.setEmail(doctor.getUser().getEmail());
 
        userDto.setRole(doctor.getUser().getRole());
        // Don't include password in DTO
        dto.setUser(userDto);

        // Convert availabilities
        List<DoctorAvailabilityDto> availabilityDtos = availabilities.stream()
                .map(avail -> {
                    DoctorAvailabilityDto availDto = new DoctorAvailabilityDto();
                    availDto.setId(avail.getId());
                    availDto.setDoctorId(doctor.getId());
                    availDto.setDayOfWeek(avail.getDayOfWeek());
                    availDto.setStartTime(avail.getStartTime());
                    availDto.setEndTime(avail.getEndTime());
                    availDto.setAvailable(avail.isAvailable());
                    availDto.setAppointmentDuration(avail.getAppointmentDuration());
                    return availDto;
                })
                .collect(Collectors.toList());
        
        dto.setAvailabilities(availabilityDtos);
        return dto;
    }
    
  
    
    public Patient registerPatient(PatientRegistrationDTO registrationDTO) {
        // Create User first
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));// Consider encrypting password
        user.setRole(registrationDTO.getRole());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        // Save user
        User savedUser = userRepository.save(user);
        
        // Create Patient
        Patient patient = new Patient();
        patient.setUser(savedUser);
        patient.setDateOfBirth(registrationDTO.getDateOfBirth());
        patient.setGender(registrationDTO.getGender());
        patient.setContactNumber(registrationDTO.getContactNumber());
        
        return patientRepository.save(patient);
    }
    
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
    
    public Optional<Patient> getPatientById(String id) {
        return patientRepository.findById(id);
    }
    
    public Optional<Patient> getPatientByUserId(String userId) {
        return patientRepository.findByUserId(userId);
    }
    
    public Optional<Patient> getPatientByEmail(String email) {
        return patientRepository.findByUserEmail(email);
    }
    
    public Patient updatePatient(String id, Patient updatedPatient) {
        Optional<Patient> existingPatient = patientRepository.findById(id);
        if (existingPatient.isPresent()) {
            Patient patient = existingPatient.get();
            
            // Update patient fields
            if (updatedPatient.getDateOfBirth() != null) {
                patient.setDateOfBirth(updatedPatient.getDateOfBirth());
            }
            if (updatedPatient.getGender() != null) {
                patient.setGender(updatedPatient.getGender());
            }
            if (updatedPatient.getContactNumber() != null) {
                patient.setContactNumber(updatedPatient.getContactNumber());
            }
            
            // Update user fields if provided
            if (updatedPatient.getUser() != null) {
                User user = patient.getUser();
                if (updatedPatient.getUser().getUsername() != null) {
                    user.setUsername(updatedPatient.getUser().getUsername());
                }
                if (updatedPatient.getUser().getEmail() != null) {
                    user.setEmail(updatedPatient.getUser().getEmail());
                }
                user.setUpdatedAt(LocalDateTime.now());
                userRepository.save(user);
            }
            
            return patientRepository.save(patient);
        }
        return null;
    }
    
    public boolean deletePatient(String id) {
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isPresent()) {
            // Delete patient first
            patientRepository.deleteById(id);
            // Optionally delete associated user
            if (patient.get().getUser() != null) {
                userRepository.deleteById(patient.get().getUser().getId());
            }
            return true;
        }
        return false;
    }
    
    public boolean existsByUser(User user) {
        return patientRepository.existsByUser(user);
    }
    
    public Optional<User> getUserById(String id) {
		return userRepository.findById(id);
    }
    
    public Admin updateAdmin(String id, Admin updatedPatient) {
	    Optional<Admin> optionalPatient = adminRepository.findById(id);
	    if (optionalPatient.isPresent()) {
	        Admin existingPatient = optionalPatient.get();
	        
	        // Update fields as needed, for example:
	        existingPatient.setDateOfBirth(updatedPatient.getDateOfBirth());
	        existingPatient.setGender(updatedPatient.getGender());
	        existingPatient.setContactNumber(updatedPatient.getContactNumber());

	        // Update profile photo:
	        existingPatient.setProfilePhoto(updatedPatient.getProfilePhoto());

	        // Optionally update user fields...
	        
	        return adminRepository.save(existingPatient);
	    }
	    return null;
	}
    public Optional<Admin> getByAdminId(String userId) {
		return adminRepository.findById(userId);
	}

	public List<Admin> getAllAdmins() {
		return adminRepository.findAll();
	}

	public Optional<Admin> getAdminById(String id) {
		return adminRepository.findById(id);
	}
	
	public Optional<Admin> getAdminByUserId(String userId) {
		return adminRepository.findByUser_Id(userId);
	}
}
