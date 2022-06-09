# spring-resilience4j
  Projeto criado com destino a testar a biblioteca Resilience4j em susbtituição ao Hystrix

## Pré-requisitos
- JDK 8;
- Spring Boot 2.1.x;
- Resilience4j 1.1.x (a última versão do resilience4j é a 1.3 mas o resilience4j-spring-boot2 utiliza a versão 1.1.x);
- IDE;
- Gradle;
- NewRelic APM (Ou Prometheus com Grafana).


## Resilience4j
O Resilience4j foi inspirado no Hystrix da Netflix, porém, foi desenvolvido em Java 8 e utilizando programação funcional. É bem leve, pois tem apenas a biblioteca Vavr como dependência, já o Hystrix tem como dependência o Archaius, que possui várias outras dependências externas, como o Guava e o Apache Commons.

Uma nova biblioteca sempre terá vantagem quando comparada com uma biblioteca mais antiga, já que a novata pode aprender com os erros do seu antecessor. Além disso, o Resilience4j vem com diversas funcionalidades novas:

![image](https://user-images.githubusercontent.com/52466816/172963733-ce0253ad-300d-441a-84d0-dbdb07191f7f.png)

### Circuit Breaker
Quando um serviço invoca outro serviço, sempre há a possibilidade de que o serviço externo não esteja executando ou a latência esteja muito alta. Isso pode levar à exaustão do número de threads, pois as mesmas estarão esperando outras requisições terminarem. O pattern de Circuit Breaker funciona de maneira similar a um Circuit Breaker elétrico:

Quando um número de falhas consecutivas ultrapassa determinado limite, o Circuit Breaker se abre;
Durante certo tempo, todas as requisições invocando este serviço remoto irão falhar imediatamente;
Após este período, o Circuit Breaker permite que um determinado número de requisições de testes passem;
Se estas requisições de testes terminarem com sucesso, o Circuit Breaker se fecha e volta a operar normalmente;
Caso contrário, se continuar havendo falhas, o período sem requisições ao serviço externo é reiniciado.

### Rate Limiter
O Rate Limiter garante que um serviço só aceite determinado número de requisições durante uma janela de tempo, garantindo que os recursos sejam utilizados de acordo com os limites desejados e que não sejam utilizados até a sua exaustão.

### Retry
O Retry permite que uma aplicação trate falhas momentâneas quando fizerem chamadas para serviços externos, garantindo que retentativas sejam feitas por um certo número de vezes. Caso não obtenham sucesso após todas as retentativas, a chamada ao método falha e a resposta deve ser tratada normalmente pela aplicação.

### Bulkhead
O Bulkhead garante que a falha em uma parte do sistema não cause o falha no sistema todo. Ele controla o número de chamadas concorrentes que um componente pode ter. Dessa maneira, o número de recursos esperando resposta do componente é limitado. Há dois tipos de implementações do bulkhead:

O Isolamento por semáforo: limita o número de chamadas concorrentes ao serviço, rejeitando imediatamente outras chamadas assim que o limite é alcançado;
O isolamento por thread pool: utiliza um thread pool para separar o serviço dos consumidores e limita cada consumidor a um subgrupo dos recursos do sistema.
O abordagem por thread pool também provê uma fila de espera, rejeitando requisições apenas quando o pool e a fila estão cheias. O gerenciamento da thread pool adiciona um pouco desobrecarga, o que diminui um pouco a performance quando comparado ao uso de semáforos, mas permite que threads fiquem suspensas até expirar, caso não sejam executadas.
