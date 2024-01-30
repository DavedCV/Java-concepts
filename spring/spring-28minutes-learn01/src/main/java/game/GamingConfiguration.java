package game;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class GamingConfiguration {

    @Bean
    @Primary
    public GameConsole marioGame() {
        return new MarioGame();
    }

    @Bean
    public GameConsole superContraGame() {
        return new SuperContraGame();
    }

    @Bean
    public GameRunner gameRunner(GameConsole gameConsole) {
        return new GameRunner(gameConsole);
    }
}
