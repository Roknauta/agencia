# Padrão para criação de CRUDs

## Entidades de domínio e camadas associadas

- Ao incluir ou alterar qualquer entidade em `domain`, manter sincronizadas as camadas correspondentes: DTO, Repository, Service e Mapper, nos pacotes aplicáveis ao domínio.
- Atualizar também DTOs, mapeamentos, filtros e associações das entidades que referenciem o domínio alterado. Para entidades documentais, considerar também as coleções comuns herdadas de `DocumentoViagem`.
- Uma renomeação ou alteração de entidade deve ser propagada por todas as referências do projeto: entidades associadas, DTOs, repositories e consultas JPQL, services, mappers, controllers, converters, telas XHTML, menus, mensagens, especificações, relatórios Jasper e testes. Renomear também os arquivos e classes correspondentes, removendo referências obsoletas ao nome anterior.
- A mesma sincronização vale para qualquer campo de domínio incluído, removido, renomeado ou alterado: atualizar DTOs espelho, filtros e persistência, services/mappers, telas e mensagens, relatórios e seus DTOs Jasper, especificações e testes. Verificar os templates Jasper para garantir que não acessem propriedades antigas nem exibam nomes obsoletos.
- Entidades e associações devem ser refletidas como DTOs correspondentes; resolver referências persistentes no Service e delegar filtros ao Repository.

## Estrutura, nomes e pacotes

Cada cadastro deve ter suas próprias classes de entidade, DTO, repository, service, mapper, controller e converter, nomeadas `<Entidade>`, `<Entidade>DTO`, `<Entidade>Repository`, `<Entidade>Service`, `<Entidade>Mapper`, `<Entidade>Controller` e `<Entidade>Converter`.

- Não informar o atributo `name` na anotação `@Table` das entidades; usar `@Table` sem nome somente quando a anotação for necessária.
- Não adicionar anotações Lombok de builder (`@Builder`, `@SuperBuilder` ou equivalentes) às classes do CRUD.
- Converters devem converter o DTO do cadastro, nunca a entidade, e devem sempre delegar ao Service correspondente para localizar ou converter os dados.
- Cada converter CRUD deve estender `CrudConverterBase<EntidadeDTO, EntidadeService>`; a classe concreta implementa somente `getAsString`.

Criar as classes nos pacotes correspondentes: `domain`, `dto`, `repository`, `service`, `service.mapper`, `web.crud` para controllers CRUD e `web.converter` para converters.

O nome do arquivo XHTML deve derivar do nome da entidade em CamelCase, em minúsculas e com hífen entre palavras. Exemplo: `CasaNova` usa `casa-nova.xhtml`.

## Entidade e DTO

- Não usar nomes totalmente qualificados de classes em declarações de atributos, parâmetros, retornos ou variáveis locais. Declarar as dependências no bloco `import` no início do arquivo e usar o nome simples da classe no código.
- A entidade representa os dados persistidos, herdando `EntidadeBase` quando aplicável. Não copiar particularidades de outros domínios; incluir somente comportamento próprio da entidade quando necessário.
- Classes do domínio devem conter somente estado e mapeamentos; não criar métodos de negócio ou cálculo nelas. Tratar essas operações em services ou controllers.
- O DTO deve espelhar a entidade. Se uma entidade referencia outra, o DTO referencia o DTO correspondente, sem substituir a associação por campos isolados como id ou nome duplicado.

## Repository, service e mapper

- Cada entidade tem seu próprio repository.
- Cada entidade tem seu próprio mapper para converter entre entidade e DTO e atualizar registros existentes.
- Preferir o mapeamento implícito do MapStruct quando origem e destino têm propriedades de mesmo nome e tipos compatíveis. Não declarar `@Mapping(target = "campo", source = "campo")` nem copiar valores manualmente em services/mappers nesses casos; reservar mapeamentos explícitos para diferenças de nomes/tipos, associações que precisam ser ignoradas ou resolvidas no Service, projeções parciais para evitar ciclos/grafos excessivos e conversões realmente especiais.
- O service de cada cadastro coordena persistência e regras de negócio próprias.
- A pesquisa deve delegar filtros ao repository. Criar manualmente o método de consulta conforme a entidade e seus filtros; não carregar todos os registros para filtrá-los em memória.

## Controller e mensagens

- Todo controller CRUD herda de `CrudControllerBase`, informando o DTO e o Service específicos.
- O controller implementa `pageTitle()` e retorna, por `Msg`, o valor da chave de título em `messages.properties`.
- A chave de título deriva do nome CamelCase da entidade, em minúsculas e com palavras separadas por pontos. Exemplo: `CasaNova` usa `casa.nova=Casa Nova`.
- Usar a mesma chave como rótulo do item no menu.

## Página XHTML e menu

- Toda página CRUD usa `/layout/crud.xhtml` como template.
- Declarar `crudController` apontando para o controller do cadastro e `formId` com o nome da entidade iniciado em minúscula (por exemplo, `CasaNova` usa `casaNova`).
- Declarar os `ui:define` necessários para preencher os `ui:insert` de `crud.xhtml`.
- A tela abre na pesquisa, sem resultados até a ação Pesquisar. Novo e Editar exibem o formulário. Depois de salvar ou excluir, atualizar os resultados mantendo os filtros. Excluir pelo id.
- O menu em `layout/navigation.xhtml` deve ter o submenu `Cadastro`. Adicionar o CRUD nele, salvo solicitação de outro submenu, usando a chave de título como rótulo.
- Na área de resultados, usar o composite `resources/components/crudDataTable.xhtml`. Declarar as colunas com a tag Facelets `resources/tags/column.xhtml`; não criar diretamente `p:dataTable` ou `p:column` na página CRUD.

## Campos e composites

- Campos de formulário usam a composição de rótulo acima, entrada no meio e mensagem de validação abaixo, seguindo `resources/components/inputText.xhtml`.
- Todo componente de entrada usado em formulário deve ter um composite com essa composição. Ao criar um CRUD, verificar se o composite do componente necessário existe; se não existir, criar um usando o nome do componente PrimeFaces principal (por exemplo, `inputText` para `p:inputText`) e expondo atributos e comportamentos necessários.
- Quando um autocomplete representar outra entidade, seu `completeMethod` deve chamar o controller dessa entidade no método `complete<Entidade>` (por exemplo, `paisController.completePais`). Se esse método não existir, criar no service da entidade referida um método que receba `String`, comece com `find` e delegue a busca ao repository; a consulta deve retornar todos os registros quando o texto vier vazio ou não informado.
- Se um campo for uma lista, o autocomplete deve receber `multiple="true"`.
