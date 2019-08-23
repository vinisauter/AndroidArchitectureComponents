package com.vas.architecture.github.repositories.repository;

//import com.vas.architecture.github.repositories.repository.datasource.RetrofitDataSource;
//import com.vas.architecture.github.repositories.repository.datasource.RoomDataSource;
//
//public class GitHubRepoRepository {
//    private RetrofitDataSource retrofitDataSource = new RetrofitDataSource();
//    private RoomDataSource roomDataSource = new RoomDataSource();
//
//    //TODO: Quem seta este valor?
//    // Pode ser um DataSource sobre o estado da internet ou vem do Model? Ou pode vir dos 2
//    private boolean hasInternet = true;
//
//    public GitHubRepoRepository() {
//
//    }
//    // TODO: Caso a interface no DataSource
//    public DataSource.Factory<Integer, Repo> getRepositories(String query) {
//        if (hasInternet) {
//            // FIXME: Na chamada de uma rest pode ocorrer algum erro na busca da pagina como tratar isso?
//            return retrofitDataSource.getRepositoriesRestFactory(query).map(input -> {
//                // Salvar itens no Banco de dados
//                // FIXME: neste caso não tenho toda informação nessessaria para criar e popular o objeto do BD
//                // roomDataSource.save(input);
//                return input;
//            });
//        } else {
//            return roomDataSource.getRepositoriesDBFactory(query);
//        }
//    }
//}
