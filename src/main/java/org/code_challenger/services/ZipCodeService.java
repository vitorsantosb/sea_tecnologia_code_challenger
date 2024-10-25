package org.code_challenger.services;

import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class ZipCodeService {

    @Getter
    @Setter
    public static class AddressResponse {
        private String cep;
        private String logradouro;
        private String complemento;
        private String bairro;
        private String localidade;
        private String uf;
        private String ibge;
        private String gia;
        private String ddd;
        private String siafi;
    }

    public static boolean VerifyZipCode(String _zipCode) {
        _zipCode = _zipCode.trim().replaceAll("[^0-9]", "");
        String _uri = "https://viacep.com.br/ws/" + _zipCode + "/json/";

        try {
            RestTemplate restTemplate = new RestTemplate();
            AddressResponse result = restTemplate.getForObject(_uri, AddressResponse.class);

            return result != null && result.getCep() != null && !result.getCep().isEmpty();
        } catch (HttpClientErrorException.BadRequest e) {
            System.out.println("invalid ZipCode: " + _zipCode);
            return false;
        } catch (Exception e) {
            System.out.println("Failure for consult zipCode: " + e.getMessage());
            return false;
        }
    }
}

