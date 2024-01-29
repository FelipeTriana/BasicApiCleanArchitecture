package cleanarchitecture.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = WebEntryConfig.class)
public class WebEntryConfigTest {

    @Autowired
    ApplicationContext context;

    @Mock
    HttpHandler handler;

    @Mock
    Environment env;

    @InjectMocks
    WebEntryConfig webEntryConfig;

    @Test
    public void shouldExistsNettyServerFactoryBean() {
        Assertions.assertTrue(context.containsBean("nettyReactiveWebServerFactory"));
        Assertions.assertNotNull(context.getBean(NettyReactiveWebServerFactory.class));
    }

    @Test
    public void shouldConfigureWebServer() {
        when(env.getProperty("app.context")).thenReturn("/api");

        NettyReactiveWebServerFactory factory = webEntryConfig.nettyReactiveWebServerFactory();

        StepVerifier.create(Mono.just(factory.getWebServer(handler))).assertNext(server -> {
            assertThat(server).isNotNull();
            assertThat(server).isInstanceOf(WebServer.class);
        }).verifyComplete();
    }
}
