# Shelly Mock API

O projeto Shelly Mock API tem o objetivo de emular a API de um grupo restrito de dispositivos e auxiliar o desenvolvimento de aplicações que façam uso deste tipo de aparelhos.




# Iniciar

Para iniciar o emulador da API shelly, deve executar o comando npm especificando o número de dispositivos que pretende emular:
- `npm start (blind numberX plug numberY light numberZ)`

Neste comando, as palavras numberX, numberY e numberZ devem ser substituídas por um número adequado entre 1 e 10. Variações deste comando são também possíveis desde que exista pelo menos um tipo de servidores a serem iniciados.

Exemplos práticos podem ser encontrados aqui:
- `npm start plug 1 light 2 blind 3`
- `npm start plug 2 blind 3`
- `npm start plug 1`


# Aparelhos

Foram emulados um total de 3 aparelhos:
- Estores
- Tomadas
- Luzes RGB

## Estores
- http://localhost:3000/status
- http://localhost:3000/roller/0?go=open
- http://localhost:3000/roller/0?go=close
- http://localhost:3000/roller/0?go=to_pos&roller_pos=100
- http://localhost:3000/meters/0
- http://localhost:3000/meters/1
## Tomada
- http://localhost:3000/status
- http://localhost:3000/relay/0?turn=on
- http://localhost:3000/relay/0?turn=on&duration=10
- http://localhost:3000/relay/0?turn=off
- http://localhost:3000/relay/0?turn=toggle
- http://localhost:3000/meters/0
## Luz
- http://localhost:3000/status
- http://localhost:3000/light/0?turn=on
- http://localhost:3000/light/0?turn=on&mode=color&red=10&green=10&blue=10&gain=10
- http://localhost:3000/light/0?turn=on&mode=white&white=10&brightness=100
- http://localhost:3000/relay/0?turn=off
- http://localhost:3000/meters/0
