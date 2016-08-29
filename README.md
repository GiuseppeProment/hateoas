##conquest challenge

mvn install - run all unit and integration tests

mvn spring-boot:run - if you want to run by yourself

https://the-profile.herokuapp.com/

-- Remarks

1) A especificação pede para enviar o token como atributo do Json, eu não faria isso e a enviaria como header do response.

2) A especificação não pede para gerar um novo token no login, nesse caso o token ficaria congelado no banco e como foi pedido para usar um hash a aplicação seria inviável, nesse caso eu gero um token no login.

[]'s
