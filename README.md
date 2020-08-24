# Star Wars Planets API

## Para executar os testes:
  ### Pré requisitos: 
    openjdk 11
    maven
  
  ### Executar: 
    mvn clean test

## Para executar a api:
  ## Pré requisitos:
    docker-compose
  
  ### Executar:
    docker-compose build && docker-compose up
  
  ## Observações:    
  
  - Os endpoints ficarão disponíveis para consulta no endereço: http://localhost:8080/api/swagger-ui.html
  
  - **O endpoint para listagem de todos os planetas do banco de dados está sendo sobrescrito pelo endpoint de busca por nome no swagger e por isso não está sendo apresentado**
  
  - Abaixo é posível importar os endpoints para teste no Insomnia
  [![Run in Insomnia}](https://insomnia.rest/images/run.svg)](https://insomnia.rest/run/?label=Star%20Wars%20Planets%20API&uri=https%3A%2F%2Fgithub.com%2Fvictorbrugnolo%2Fstar-wars-planets-api%2Fblob%2Fmaster%2FStar%20Wars%20Planets%20API.json)
  
  
