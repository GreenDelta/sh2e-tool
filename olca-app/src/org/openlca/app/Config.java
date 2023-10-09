package org.openlca.app;

/**
 * Contains the configuration values of the application. Some of these values
 * can be overwritten via command line arguments (see {@link AppArg}).
 */
public final class Config {

	private Config() {
	}

	/**
	 * The name of the application that is shown to the user.
	 */
	public static final String APPLICATION_NAME = "sh2e-tool";

	/**
	 * The name of the workspace folder. This folder is located in the user
	 * directory (Java property "user.home"). The name of this folder is
	 * "openLCA-data", but it can be renamed in development versions (so that
	 * the users can run multiple versions of openLCA in parallel).
	 */
	public static final String WORK_SPACE_FOLDER_NAME = "openLCA-data-1.4";

	/**
	 * Indicates if the workspace folder should be located in the user dir. If
	 * false the workspace will be created in the installation directory of
	 * openLCA
	 */
	public static final boolean WORK_SPACE_IN_USER_DIR = true;

	/**
	 * The name of default folder where the local databases are stored. This
	 * folder is located in the workspace directory.
	 */
	public static final String DATABASE_FOLDER_NAME = "databases";

	/**
	 * Link to the openLCA online help.
	 */
	public static final String HELP_URL = "http://www.openlca.org/manuals";

}
