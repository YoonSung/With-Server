package gaongil.config;

import gaongil.support.Constant;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource("classpath:/database.properties")

//JPA Repository Class Scanning
@EnableJpaRepositories(basePackages={"gaongil.repository"})

//Enable Transaction Annotation Setting. It is same with <tx:annotation-driven/>
@EnableTransactionManagement
public class DBConfig {
	
	@Autowired
	Environment environment;
	
	@Bean(destroyMethod="close")
	public DataSource dataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		
		String driverClassName = environment.getProperty(Constant.PROPERTY_KEY_DB_DRIVERCLASSNAME);
		String url = environment.getProperty(Constant.PROPERTY_KEY_DB_URL);
		String username = environment.getProperty(Constant.PROPERTY_KEY_DB_USERNAME);
		String password = environment.getProperty(Constant.PROPERTY_KEY_DB_PASSWORD);
		
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		
		entityManagerFactoryBean.setDataSource(dataSource());
		entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		entityManagerFactoryBean.setPackagesToScan("gaongil.domain");
		entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);

		String jpaDialect = Constant.PROPERTY_KEY_JPA_DIALECT;
		String jpaFormatSql = Constant.PROPERTY_KEY_JPA_FORMATSQL;
		String jpaNamingStrategy = Constant.PROPERTY_KEY_JPA_NAMING_STRATEGY;
		String jpaShowSql = Constant.PROPERTY_KEY_JPA_SHOWSQL;
		String jpaOperationMode=Constant.PROPERTY_KEY_JPA_JPATODDL;
		
		Properties jpaProperties = new Properties();
		jpaProperties.put(jpaDialect, environment.getProperty(jpaDialect));
		jpaProperties.put(jpaFormatSql, environment.getProperty(jpaFormatSql));
		jpaProperties.put(jpaNamingStrategy, environment.getProperty(jpaNamingStrategy));
		jpaProperties.put(jpaShowSql, environment.getProperty(jpaShowSql));
		jpaProperties.put(jpaOperationMode, environment.getProperty(jpaOperationMode));

		/*
		jpaProperties.put("hibernate.cache.provider_class", "org.hibernate.cache.SingletonEhCacheProvider");
		jpaProperties.put("hibernate.cache.use_second_level_cache", "false");
		jpaProperties.put("hibernate.cache.use_query_cache", "false");
		*/

		//jpaProperties.put("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");

		entityManagerFactoryBean.setJpaProperties(jpaProperties);
		return entityManagerFactoryBean;
	}
	
	@Bean
	public JpaTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(
				entityManagerFactory().getObject()
		);
		
		return transactionManager;
	}
}
