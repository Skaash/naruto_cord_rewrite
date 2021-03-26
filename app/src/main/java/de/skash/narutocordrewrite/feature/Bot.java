/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package de.skash.narutocordrewrite.feature;

import de.skash.narutocordrewrite.core.api.RequestFactory;
import de.skash.narutocordrewrite.core.cache.CommandCache;
import de.skash.narutocordrewrite.core.cache.PlayerCache;
import de.skash.narutocordrewrite.core.cache.ServerCache;
import de.skash.narutocordrewrite.core.command.CommandHandler;
import de.skash.narutocordrewrite.core.repository.ApiPlayerRepository;
import de.skash.narutocordrewrite.core.repository.ApiServerRepository;
import de.skash.narutocordrewrite.core.repository.IPlayerRepository;
import de.skash.narutocordrewrite.core.repository.IServerRepository;
import net.dv8tion.jda.api.GatewayEncoding;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class Bot {
    private static final Logger LOG = LoggerFactory.getLogger(Bot.class);

    private final RequestFactory requestFactory = new RequestFactory();

    private final CommandCache commandCache = new CommandCache();
    private final PlayerCache playerCache = new PlayerCache();
    private final ServerCache serverCache = new ServerCache();

    private final IServerRepository serverRepository = new ApiServerRepository(requestFactory, serverCache);
    private final IPlayerRepository playerRepository = new ApiPlayerRepository(requestFactory, playerCache);

    private final CommandHandler commandHandler = new CommandHandler(commandCache, playerCache, playerRepository);

    private Bot() {
        try {
            JDABuilder
                    .createDefault(System.getenv("NARUTO_CORD_TOKEN"))
                    .disableCache(
                            CacheFlag.VOICE_STATE,
                            CacheFlag.ACTIVITY,
                            CacheFlag.MEMBER_OVERRIDES,
                            CacheFlag.CLIENT_STATUS,
                            CacheFlag.ROLE_TAGS
                    )
                    .setMemberCachePolicy(MemberCachePolicy.NONE)
                    .setChunkingFilter(ChunkingFilter.NONE)
                    .setStatus(OnlineStatus.DO_NOT_DISTURB)
                    .setGatewayEncoding(GatewayEncoding.ETF)
                    .build().awaitReady();
            LOG.info("Bot started successfully...");
        } catch (final LoginException | InterruptedException e) {
            LOG.error("Error while appeared while building the bot...", e);
        }
    }

    public static void main(String[] args) {
        new Bot();
    }
}
