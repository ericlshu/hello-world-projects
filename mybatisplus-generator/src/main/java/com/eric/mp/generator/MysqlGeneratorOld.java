package com.eric.mp.generator;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-03-17 17:34
 */
public class MysqlGeneratorOld {

    /*public static String scanner(String tip)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please input" + tip + " : ");
        if (scanner.hasNext())
        {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt))
            {
                return ipt;
            }
        }
        throw new MybatisPlusException("Please input correct " + tip + " !");
    }

    public static void main(String[] args)
    {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        System.out.println("projectPath = " + projectPath);
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("Eric SHU");
        gc.setOpen(false);
        mpg.setGlobalConfig(gc);
        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&useSSL=false&characterEncoding=utf8");
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUsername("eric");
        dataSourceConfig.setPassword("1234");
        mpg.setDataSource(dataSourceConfig);
        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(scanner("package name"));
        pc.setParent("com.eric.mp.generator");
        mpg.setPackageInfo(pc);
        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap()
            {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo)
            {
                // 自定义输入文件名称
                String outputFile =  projectPath + "/src/main/resources/mapper/"
                        + pc.getModuleName()
                        + "/" + tableInfo.getEntityName()
                        + "Mapper" +
                        StringPool.DOT_XML;
                System.out.println("outputFile = " + outputFile);
                return outputFile;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.setTemplate(new TemplateConfig().setXml(null));
        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperEntityClass("com.baomidou.mybatisplus.samples.generator.common.BaseEntity");
        strategy.setEntityLombokModel(true);
        strategy.setSuperControllerClass("com.baomidou.mybatisplus.samples.generator.common.BaseController");
        strategy.setInclude(scanner("table name"));
        strategy.setSuperEntityColumns("id");
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        // 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }*/
}
