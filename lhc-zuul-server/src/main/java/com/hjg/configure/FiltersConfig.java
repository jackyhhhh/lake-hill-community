package com.hjg.configure;

import com.hjg.filter.CheckLoginStatusFilter;
import org.springframework.context.annotation.Bean;

public class FiltersConfig {

    @Bean
    public CheckLoginStatusFilter checkLoginStatusFilter(){return new CheckLoginStatusFilter();}
}
