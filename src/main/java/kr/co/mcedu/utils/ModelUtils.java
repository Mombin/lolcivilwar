package kr.co.mcedu.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ModelUtils {
    private static final ModelMapper MAPPER;
    static {
        MAPPER = new ModelMapper();
        MAPPER.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public static <D> D map(Object source, Class<D> destinationType) {
        return MAPPER.map(source, destinationType);
    }
}