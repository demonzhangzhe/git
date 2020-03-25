package cn.hybris.boot.configure;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;

/**
 * Mybatis-Plus配置类
 * 
 * @author Qu Dihuai
 *
 */
@Configuration
@EnableTransactionManagement
@MapperScan("cn.hybris.core.dao")
public class MybatisPlusConfiguration {
	/**
	 * 分页
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		// 开启count的join优化,只针对部分left join
		paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
		return paginationInterceptor;
	}
}
