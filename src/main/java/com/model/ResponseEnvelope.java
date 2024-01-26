package com.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.objenesis.SpringObjenesis;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class ResponseEnvelope {
        @JsonProperty("message")
        private String message;

        @JsonProperty("status")
        @Valid
        private Integer status;

        @JsonProperty("content")
        @Valid
        private List<RawProduct> content;

        public ResponseEnvelope(String message, Integer status){
                this.message = message;
                this.status = status;
        }

        public ResponseEnvelope(String message, Integer status, List<RawProduct> content){
                this.message = message;
                this.status = status;
                this.content = content;
        }

}
