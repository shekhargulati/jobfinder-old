package com.jobsnearyou.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.jobsnearyou.account.JdbcAccountRepository;
import com.mongodb.Mongo;

@Configuration
@ComponentScan(basePackages = "com.jobsnearyou", excludeFilters = { @Filter(Configuration.class) })
@PropertySource("classpath:com/jobsnearyou/config/application.properties")
@ImportResource("classpath:com/jobsnearyou/config/task.xml")
@EnableTransactionManagement
public class MainConfig {

	@Bean(destroyMethod = "shutdown")
	public DataSource dataSource() {
		EmbeddedDatabaseFactory factory = new EmbeddedDatabaseFactory();
		factory.setDatabaseName("jobsnearyou");
		factory.setDatabaseType(EmbeddedDatabaseType.H2);
		factory.setDatabasePopulator(databasePopulator());
		return factory.getDatabase();
	}
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}
	
	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}

	@Bean
	public PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	// internal helpers

	private DatabasePopulator databasePopulator() {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("JdbcUsersConnectionRepository.sql", JdbcUsersConnectionRepository.class));
		populator.addScript(new ClassPathResource("Account.sql", JdbcAccountRepository.class));
		return populator;
	}
	
	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbOpenShiftFactory());
		return mongoTemplate;
	}
	
	@Bean
	public MongoDbFactory mongoDbFactory() throws Exception {
		Mongo mongo = new Mongo("localhost", 27017);
		String databaseName = "jobsnearyou";
		MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongo,
				databaseName);
		return mongoDbFactory;
	}
	
	@Bean
	public MongoDbFactory mongoDbOpenShiftFactory() throws Exception {
		String openshiftMongoDbHost = System.getenv("OPENSHIFT_NOSQL_DB_HOST");
		int openshiftMongoDbPort = Integer.parseInt(System.getenv("OPENSHIFT_NOSQL_DB_PORT"));
		String username = System.getenv("OPENSHIFT_NOSQL_DB_USERNAME");
		String password = System.getenv("OPENSHIFT_NOSQL_DB_PASSWORD");
		Mongo mongo = new Mongo(openshiftMongoDbHost, openshiftMongoDbPort);
		UserCredentials userCredentials = new UserCredentials(username,password);
		String databaseName = "jobsnearyou";
		MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongo, databaseName, userCredentials);
		return mongoDbFactory;
	}
}
