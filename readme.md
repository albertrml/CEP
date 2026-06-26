# CEP - Busca e Gerenciamento de Endereços

## Visão Geral

O CEP é um aplicativo Android moderno desenvolvido para facilitar a consulta e o armazenamento de 
endereços brasileiros. O objetivo principal deste projeto é demonstrar a aplicação de práticas de 
engenharia de software de alto nível, como Clean Architecture, SOLID e Testes de UI/Unitários, 
aplicados ao ecossistema moderno do Android.

O app permite buscar informações detalhadas a partir de um CEP ou localizar um CEP a partir de um 
endereço (Rua, Cidade, UF), oferecendo suporte a favoritos e cache local para consultas offline.

---

## Status do Projeto

 - 1.0: 
   - Versão inicial do projeto.
   - Arquitetura MVVM/MVI
   - Consulta CEP e retorna endereço completo.
 - 2.0: 
   - Publicação da aplicação na google play
   - Via room/SQLite, Persistência de
     - Histórico de busca
     - Cache de busca e resultados
     - Endereços completos favoritos com sua respectiva anotação
   - Layout responsivo com **`NavigableListDetailPaneScaffold`**, garantido uso em smartphone e tablets
   - Navegação por **`NavigationSuiteScaffold`** e **`NavigableListDetailPaneScaffoldStateHolder`**
   - Importação e exportação dos favoritos
   - Temas dark e light e reformulação das cores e visuais

 - 3.0 (em andamento): 
   - Restruturação da arquitetura para MVI apenas
   - Normalização e migração do Room/SQLite para permitir
     - Multiplas anotações por favoritos
     - Manutenção simplificada
     - Adição de filtros avançados
     - Separação das responsabilidades por funcionalidades
     - Os favoritos exportados da versão 2.0 não são suportados como fonte de importação, embora o 
     esquema contido no banco de dados é migrado normalmente para o novo esquema desta versão.
   - Implementação da busca por endereço completo a partir do endereço parcial (Rua, Cidade, UF)
   - Refatoração de UI
     - Mudanças em cores, fontes e layouts
     - Os favoritos e cache são dispostos em **`FastScrollGrid`**
     - O histórico de busca é exibido em **`FastScrollColumn`** em razão do **`stickyHeader`**, que 
     garante a separação do histórico por datas.


A versão 2.0 encontra-se disponível na [Google Play](https://play.google.com/store/apps/details?id=br.com.arml.cep)

---

## Funcionalidades Principais

- Busca Dupla: Pesquisa por número de CEP ou por logradouro completo.
- Interface Adaptativa: Uso de ListDetailPaneScaffold para suporte nativo a tablets, dobráveis e 
diferentes orientações de tela.
- Cache Inteligente: Armazenamento automático de consultas recentes para acesso rápido sem consumo 
de dados.
- Favoritos: Gerenciamento de endereços salvos para consulta frequente.
- Histórico (Logs): Rastreabilidade de todas as pesquisas realizadas com filtros avançados

---

## Tecnologias e Ferramentas

Este projeto utiliza o que há de mais moderno no desenvolvimento Android:

- **Arquitetura**: Inicialmente, desenvolvido em MVVM/MVI; agora, MVI apenas.
- **Interface**: Jetpack Compose com Material 3.
- **Injeção de Dependência**: Hilt.
- **Linguagem**: Kotlin (Coroutines, Flow).
- **Persistência**: Room Database (para cache, favoritos e histórico de busca).
- **Rede**: Retrofit & OkHttp.
- **Testes**: 
  - **Unitários**: JUnit 5 & Mockk.
  - **Instrumentação (UI)**: Compose Test Rule e Semantics.

---

## Arquitetura e Engenharia

O projeto foi estruturado para ser escalável e testável, separando responsabilidades de forma clara:

1. Domain Layer: Contém as entidades de negócio, exceções customizadas e definições de repositórios 
(independente de frameworks).
2. Data Layer: Implementação dos repositórios, fontes de dados (Remote/Local) e mapeadores (Mappers).
3. UI Layer: Composta por ViewModels que gerenciam o estado da tela (UiState) e Composables puras 
que reagem a esses estados.

### Destaque Técnico: Adaptive Layout

Diferente de apps comuns, este projeto utiliza o **`NavigableListDetailPaneScaffold`**, permitindo 
que em telas grandes (Tablets/Desktop) a lista de busca e os detalhes do endereço apareçam lado a 
lado, enquanto em celulares a navegação é automática entre telas.

---

## Qualidade de Código

A cobertura de testes é uma prioridade. O projeto conta com:
- Testes de Componentes: Verificação de comportamento isolado de botões, campos de texto e abas.
- Testes de Integração: Fluxos completos de busca e salvamento em cache.

---

## Screenshots

---

## Como Executar

1. Clone o repositório: git clone https://github.com/seu-usuario/cep.git
2. Abra no Android Studio Ladybug ou superior.
3. Sincronize o Gradle e execute no emulador ou dispositivo físico.

## Autor

Albert R Moraes L
- [Linkedin](https://www.linkedin.com/in/albert-richard-73983723/)