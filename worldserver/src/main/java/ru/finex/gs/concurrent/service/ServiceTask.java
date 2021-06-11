package ru.finex.gs.concurrent.service;

import org.hibernate.Session;

/**
 * @author m0nster.mind
 */
public interface ServiceTask {

    Session getDbSession();

}
