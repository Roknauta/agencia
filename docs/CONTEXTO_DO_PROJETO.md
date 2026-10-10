# Contexto do projeto

Este projeto recria um sistema de uma agência de viagens que já existia em uma aplicação legada. A versão atual organiza os cadastros e documentos para apoiar o fluxo comercial e operacional da agência.

## Finalidades dos documentos

As entidades documentais representam serviços e dados reutilizáveis para três etapas:

- **Orçamento:** proposta apresentada ao cliente.
- **Emissão:** confirmação e emissão dos serviços após aprovação do orçamento.
- **Contrato:** formalização dos serviços e condições acordados com o cliente.

O fluxo começa no orçamento. Quando aprovado, ele serve de base para a emissão; o contrato registra a contratação formal. Informações comuns devem ser reaproveitadas entre as etapas, preservando o registro contratado e evitando que alterações posteriores reescrevam contratos já emitidos.

## Relatórios

Os relatórios Jasper geram PDFs de orçamento, emissão e contrato. As classes em `web.jasper` são modelos específicos para apresentação dos relatórios; não substituem os DTOs espelho do domínio. Os templates ficam em `src/main/resources/reports`.

As informações contratuais devem apresentar os serviços selecionados e suas condições com clareza. Quando o contrato incorporar orçamento, anexo ou regras de fornecedores, esses documentos precisam ser identificados e entregues ao cliente junto com o contrato.

## Limites de alteração

Não alterar entidades de domínio nem seus DTOs espelho sem autorização explícita do usuário. Ajustes nos relatórios devem usar as classes próprias de Jasper sempre que possível. Ao alterar qualquer entidade de domínio, seguir o `AGENTS.md` e sincronizar as camadas associadas.

Toda alteração ou renomeação de entidade ou campo de domínio deve ser refletida em todas as referências do projeto, incluindo associações, DTOs, repositories e consultas, services, mappers, controllers, converters, telas, menus, mensagens, especificações, relatórios Jasper e testes. Arquivos e identificadores devem acompanhar o novo nome; referências obsoletas devem ser removidas. Conferir também expressões e fields usados diretamente pelos templates Jasper.
