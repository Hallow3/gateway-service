package com.soft.gatewayservice.filter;

import com.soft.gatewayservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

//annotation de composant
@Component
//cette classe doit hériter de AbstractGatewayFilterFactory
//elle doit avoir une classe interne config
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    //on injecte le validateur pour les routes à ignorer
    @Autowired
    private RouteValidator routeValidator;

    //on injecte restTemplate pour la communication entre api
    //@Autowired
    //private RestTemplate restTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    //cette methode porte la logique pour le filtre
    //mais il faut la dépendance Webflux
    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
            //ici la logique
            //si une route ne respecter pas la liste du validateur elle est filter et authentifiée
            if(routeValidator.isSecured.test(exchange.getRequest())){
                //header contains token or not
                if( ! exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                   throw new SecurityException("missing header... entete manquante");
                }
                //si lentete est presente
                System.out.println("entete presente");
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                //le header doit etre reduit pour enlever les 7 premiers caracteres
                if(authHeader != null && authHeader.startsWith("Bearer ")){
                    authHeader = authHeader.substring(7);
                }
                try{
                    //methode non sécurisé
                    //appel a auth service grace à RestTemplate de WebClient
                    //restTemplate.getForObject("http://localhost:8888/AUTHENTICATION-SERVICE/client/validate?token"+authHeader, String.class);
                    //utiliser la dependance jwt c'est mieux et valider le jeton ici
                    System.out.println("validation de la token");
                    jwtUtil.validateToken(authHeader);
                }catch (Exception e){
                    throw new SecurityException("error while trying to validate token");
                }
            }
            System.out.println("envoie de la requette a lurl: "+exchange.getRequest().getURI()+"\n" +
                    " avec comme information: \n" +
                    "methode: "+exchange.getRequest().getMethod().name()+"\n" +
                    "entête: "+exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0)+"\n" +
                    "");
            return chain.filter(exchange);
        }));
    }

    public static class Config {

    }
}
