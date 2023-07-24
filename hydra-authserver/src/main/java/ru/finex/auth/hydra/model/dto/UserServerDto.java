package ru.finex.auth.hydra.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.InetSocketAddress;

/**
 * @author m0nster.mind
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserServerDto {

    private int id;

    private InetSocketAddress address;
    private String host;
    private int port;
    private int ageLimit;
    private boolean isPvp;
    private int maxClients;

    private boolean isNormal;
    private boolean isRelax;
    private boolean isPublicTest;
    private boolean isNoLabel;
    private boolean isDeniedAvatarCreation;
    private boolean isEvent;
    private boolean isFree;

    private boolean isBrackets;

    private int online;
    private int registeredAvatars;

}
