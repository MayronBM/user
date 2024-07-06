package ni.com.user.security.mapper;

import ni.com.user.security.dto.PhoneDto;
import ni.com.user.security.model.Phone;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface PhoneMapper {

    PhoneDto convert(Phone phone);

    @InheritInverseConfiguration
    Phone convert(PhoneDto phoneDto);

    List<Phone> map(List<PhoneDto> phoneDtoList);
}
