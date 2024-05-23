package me.nycweather.peppermint.icanhazdadjoke.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DadJoke {
    private String id;
    private String joke;
    private Integer status;
}
