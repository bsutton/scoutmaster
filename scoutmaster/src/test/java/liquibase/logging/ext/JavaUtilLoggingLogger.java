/* -*- mode: Java; c-basic-offset: 2; indent-tabs-mode: nil -*-
 *
 * Copyright (c) 2011-2011 Laird Nelson.
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense and/or sell copies
 * of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT.  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 * The original copy of this license is available at
 * http://www.opensource.org/license/mit-license.html.
 */
package liquibase.logging.ext;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.logging.LogLevel;
import liquibase.logging.core.AbstractLogger;
import liquibase.logging.core.DefaultLogger; // for javadoc only
import liquibase.servicelocator.PrioritizedService; // for javadoc only

/**
 * An {@link AbstractLogger} that adapts the Java logging mechanism to the
 * <a href="http://liquibase.org">Liquibase</a>
 * {@linkplain liquibase.logging.Logger logging contracts}.
 *
 * @author <a href="mailto:ljnelson@gmail.com">Laird Nelson</a>
 *
 * @version 1.0
 *
 * @since 1.0
 */
public class JavaUtilLoggingLogger extends AbstractLogger
{

	/**
	 * An immutable {@link Map} of <a href="http://liquibase.org">Liquibase</a>
	 * {@link LogLevel} instances to {@link java.util.logging.Level} instances.
	 * This field is never {@code null} and is threadsafe.
	 */
	public static final Map<LogLevel, Level> levels;

	/**
	 * Static initializer; initializes the {@link #levels} field.
	 */
	static
	{
		final Map<LogLevel, Level> levelMap = new EnumMap<LogLevel, Level>(LogLevel.class);
		levelMap.put(LogLevel.OFF, Level.OFF);
		levelMap.put(LogLevel.DEBUG, Level.FINER);
		levelMap.put(LogLevel.INFO, Level.INFO);
		levelMap.put(LogLevel.WARNING, Level.WARNING);
		levelMap.put(LogLevel.SEVERE, Level.SEVERE);
		levels = Collections.unmodifiableMap(levelMap);
	}

	/**
	 * The {@link DatabaseChangeLog} currently in effect. This field may be
	 * {@code null} at any point.
	 *
	 * @since 1.2
	 */
	private transient DatabaseChangeLog changeLog;

	/**
	 * The {@link ChangeSet} currently in effect. This field may be {@code null}
	 * at any point.
	 *
	 * @since 1.2
	 */
	private transient ChangeSet changeSet;

	/**
	 * The underlying {@link Logger} that this {@link JavaUtilLoggingLogger}
	 * delegates to. This field may be {@code
	 * null} at any point.
	 */
	private transient Logger logger;

	/**
	 * Creates a new {@link JavaUtilLoggingLogger}. Following
	 * <a href="http://liquibase.org/">Liquibase</a> convention, callers should
	 * immediately call {@link #setName(String)} after construction.
	 */
	public JavaUtilLoggingLogger()
	{
		super();
	}

	/**
	 * Returns {@code 3} when invoked.
	 *
	 * @see PrioritizedService#getPriority()
	 */
	@Override
	public int getPriority()
	{
		return 3; // arbitrary number greater than 1
	}

	/**
	 * Sets this {@link JavaUtilLoggingLogger}'s log level to the supplied
	 * {@link LogLevel} and sets its underlying {@link Logger} implementation's
	 * {@linkplain Logger#getLevel() level} as well.
	 *
	 * @param level
	 *            the new {@link LogLevel}; may be {@code null} in which case
	 *            {@link Level#OFF} will be used instead
	 */
	@Override
	public void setLogLevel(final LogLevel level)
	{
		super.setLogLevel(level);
		if (this.logger != null && level != null)
		{
			final Level l = levels.get(level);
			if (l == null)
			{
				this.logger.setLevel(Level.OFF);
			}
			else
			{
				this.logger.setLevel(l);
			}
		}
	}

	/**
	 * Emulates the behavior of <a href="http://liquibase.org">Liquibase</a>'s
	 * {@link DefaultLogger#setLogLevel(String, String)} method by simply
	 * ignoring the second argument. This method calls
	 * {@link AbstractLogger#setLogLevel(String)}.
	 *
	 * @param logLevel
	 *            the new log level; may be {@code null}
	 *
	 * @param fileName
	 *            ignored; the {@linkplain AbstractLogger superclass} does not
	 *            document what this parameter is to be used for or what its
	 *            permissible values are
	 */
	@Override
	public void setLogLevel(final String logLevel, final String fileName)
	{
		this.setLogLevel(logLevel); // emulates DefaultLogger behavior
	}

	/**
	 * Returns the {@link LogLevel} that was previously
	 * {@linkplain #setLogLevel(LogLevel) set} on this
	 * {@link JavaUtilLoggingLogger}.
	 *
	 * <p>
	 * This implementation actually reverse engineers the {@link LogLevel} by
	 * inspecting the {@link Logger#getLevel()} of the underlying Java
	 * {@link Logger}, and using the {@link #levels} {@link Map} to locate the
	 * proper {@link LogLevel}. This allows subclasses or future versions of
	 * this class to safely expose the underlying {@link Logger} if deemed
	 * necessary.
	 * </p>
	 *
	 * <p>
	 * This method may return {@code null}.
	 * </p>
	 *
	 * @return the {@link LogLevel} used by this {@link JavaUtilLoggingLogger}
	 *         instance, or {@code null}
	 */
	@Override
	public LogLevel getLogLevel()
	{
		if (this.logger == null)
		{
			return super.getLogLevel();
		}
		final Level level = this.logger.getLevel();
		if (level == null)
		{
			return LogLevel.OFF;
		}
		final Set<Entry<LogLevel, Level>> entrySet = levels.entrySet();
		assert entrySet != null;
		assert !entrySet.isEmpty();
		for (final Entry<LogLevel, Level> entry : entrySet)
		{
			assert entry != null;
			if (level.equals(entry.getValue()))
			{
				return entry.getKey();
			}
		}
		throw new IllegalStateException();
	}

	/**
	 * Sets the name of this {@link JavaUtilLoggingLogger}.
	 *
	 * <p>
	 * This method will initialize the underlying {@link Logger} with a call to
	 * the {@link Logger#getLogger(String)} method, passing the supplied
	 * {@code name} parameter.
	 * </p>
	 *
	 * @param name
	 *            the name; may be {@code null}
	 */
	@Override
	public void setName(final String name)
	{
		this.logger = Logger.getLogger(name);
	}

	/**
	 * Returns the {@link StackTraceElement} representing the closest method
	 * invocation that is <em>not</em> an invocation of a method in this class,
	 * its superclasses or {@link java.lang.Thread}.
	 *
	 * <p>
	 * This method is used by others in this class to find the class name and
	 * method name to pass to the underlying Java {@link Logger}s when actually
	 * performing logging.
	 * </p>
	 *
	 * <p>
	 * This method may return {@code null}.
	 * </p>
	 *
	 * @return the nearest valid {@link StackTraceElement}, or {@code
	 * null}
	 */
	private final StackTraceElement getCaller()
	{
		StackTraceElement returnValue = null;
		// TODO: doPrivileged
		final StackTraceElement[] st = Thread.currentThread().getStackTrace();
		if (st != null && st.length > 0)
		{
			for (final StackTraceElement element : st)
			{
				if (element != null && element.getMethodName() != null)
				{
					final String className = element.getClassName();
					if (className != null && !className.equals("java.lang.Thread")
							&& !className.equals(this.getClass().getName()))
					{
						returnValue = element;
						break;
					}
				}
			}
		}
		return returnValue;
	}

	/**
	 * Calls the {@link #info(String, Throwable)} method, passing {@code
	 * null} as the second argument.
	 *
	 * @param message
	 *            the message to log; may be {@code null}
	 */
	@Override
	public void info(final String message)
	{
		this.info(message, null);
	}

	/**
	 * Logs the supplied message {@link String} and (optional) {@link Throwable}
	 * to the underlying {@link Logger} at the {@link Level#INFO INFO} level.
	 *
	 * @param message
	 *            the message to log; may be {@code null}
	 *
	 * @param throwable
	 *            the {@link Throwable} to log; may be {@code
	 * null}
	 */
	@Override
	public void info(final String message, final Throwable throwable)
	{
		if (this.logger != null && this.logger.isLoggable(Level.INFO))
		{
			final StackTraceElement ste = this.getCaller();
			if (throwable == null)
			{
				this.logger.logp(Level.INFO, ste.getClassName(), ste.getMethodName(), this.formatMessage(message));
			}
			else
			{
				this.logger.logp(Level.INFO, ste.getClassName(), ste.getMethodName(), this.formatMessage(message),
						throwable);
			}
		}
	}

	/**
	 * Calls the {@link #warning(String, Throwable)} method, passing {@code
	 * null} as the second argument.
	 *
	 * @param message
	 *            the message to log; may be {@code null}
	 */
	@Override
	public void warning(final String message)
	{
		this.warning(message, null);
	}

	/**
	 * Logs the supplied message {@link String} and (optional) {@link Throwable}
	 * to the underlying {@link Logger} at the {@link Level#WARNING WARNING}
	 * level.
	 *
	 * @param message
	 *            the message to log; may be {@code null}
	 *
	 * @param throwable
	 *            the {@link Throwable} to log; may be {@code
	 * null}
	 */
	@Override
	public void warning(final String message, final Throwable throwable)
	{
		if (this.logger != null && this.logger.isLoggable(Level.WARNING))
		{
			final StackTraceElement ste = this.getCaller();
			if (throwable == null)
			{
				this.logger.logp(Level.WARNING, ste.getClassName(), ste.getMethodName(), this.formatMessage(message));
			}
			else
			{
				this.logger.logp(Level.WARNING, ste.getClassName(), ste.getMethodName(), this.formatMessage(message),
						throwable);
			}
		}
	}

	/**
	 * Calls the {@link #severe(String, Throwable)} method, passing {@code
	 * null} as the second argument.
	 *
	 * @param message
	 *            the message to log; may be {@code null}
	 */
	@Override
	public void severe(final String message)
	{
		this.severe(message, null);
	}

	/**
	 * Logs the supplied message {@link String} and (optional) {@link Throwable}
	 * to the underlying {@link Logger} at the {@link Level#SEVERE SEVERE}
	 * level.
	 *
	 * @param message
	 *            the message to log; may be {@code null}
	 *
	 * @param throwable
	 *            the {@link Throwable} to log; may be {@code
	 * null}liquibase-jul-liquibase-jul
	 */
	@Override
	public void severe(final String message, final Throwable throwable)
	{
		if (this.logger != null && this.logger.isLoggable(Level.SEVERE))
		{
			final StackTraceElement ste = this.getCaller();
			if (throwable == null)
			{
				this.logger.logp(Level.SEVERE, ste.getClassName(), ste.getMethodName(), this.formatMessage(message));
			}
			else
			{
				this.logger.logp(Level.SEVERE, ste.getClassName(), ste.getMethodName(), this.formatMessage(message),
						throwable);
			}
		}
	}

	/**
	 * Calls the {@link #debug(String, Throwable)} method, passing {@code
	 * null} as the second argument.
	 *
	 * @param message
	 *            the message to log; may be {@code null}
	 */
	@Override
	public void debug(final String message)
	{
		this.debug(message, null);
	}

	/**
	 * Logs the supplied message {@link String} and (optional) {@link Throwable}
	 * to the underlying {@link Logger} at the {@link Level#FINER FINER} level.
	 *
	 * @param message
	 *            the message to log; may be {@code null}
	 *
	 * @param throwable
	 *            the {@link Throwable} to log; may be {@code
	 * null}
	 */
	@Override
	public void debug(final String message, final Throwable throwable)
	{
		if (this.logger != null && this.logger.isLoggable(Level.FINER))
		{
			final StackTraceElement ste = this.getCaller();
			if (throwable == null)
			{
				this.logger.logp(Level.FINER, ste.getClassName(), ste.getMethodName(), this.formatMessage(message));
			}
			else
			{
				this.logger.logp(Level.FINER, ste.getClassName(), ste.getMethodName(), this.formatMessage(message),
						throwable);
			}
		}
	}

	/**
	 * Formats a user-supplied log message such that it contains the same
	 * elements separated by the same delimiters as the message logged by the
	 * Liquibase {@link DefaultLogger}.
	 *
	 * <p>
	 * This method may return {@code null}.
	 * </p>
	 *
	 * @param message
	 *            the message to format; may be {@code null} in which case
	 *            {@code null} will be returned
	 *
	 * @return the formatted message, or {@code null}
	 */
	protected String formatMessage(final String message)
	{
		final String returnValue;
		if (message == null)
		{
			returnValue = null;

		}
		else
		{

			final String loggerName;
			if (this.logger == null)
			{
				loggerName = null;
			}
			else
			{
				loggerName = this.logger.getName();
			}

			final String changeLogName;
			final DatabaseChangeLog changeLog = this.getChangeLog();
			if (changeLog == null)
			{
				changeLogName = null;
			}
			else
			{
				changeLogName = changeLog.getFilePath();
			}

			final String changeSetName;
			final ChangeSet changeSet = this.getChangeSet();
			if (changeSet == null)
			{
				changeSetName = null;
			}
			else
			{
				changeSetName = changeSet.toString(false);
			}

			if (loggerName == null)
			{
				if (changeLogName == null)
				{
					if (changeSetName == null)
					{
						returnValue = message;
					}
					else
					{
						final StringBuilder sb = new StringBuilder(changeSetName);
						sb.append(": ");
						sb.append(message);
						returnValue = sb.toString();
					}
				}
				else
				{
					final StringBuilder sb = new StringBuilder(changeLogName);
					sb.append(": ");
					if (changeSetName != null)
					{
						sb.append(changeSetName);
						sb.append(": ");
					}
					sb.append(message);
					returnValue = sb.toString();
				}
			}
			else
			{
				final StringBuilder sb = new StringBuilder(loggerName);
				sb.append(": ");
				if (changeLogName == null)
				{
					if (changeSetName != null)
					{
						sb.append(changeSetName);
						sb.append(": ");
					}
				}
				else
				{
					sb.append(changeLogName);
					sb.append(": ");
					if (changeSetName != null)
					{
						sb.append(changeSetName);
						sb.append(": ");
					}
				}
				sb.append(message);
				returnValue = sb.toString();
			}
		}
		return returnValue;
	}

	/**
	 * Stores a reference to the supplied {@link DatabaseChangeLog}, which might
	 * be helpful for subclasses that wish to include it in the logging output.
	 *
	 * <h4>Implementation Notes</h4>
	 *
	 * <p>
	 * The {@link Override @Override} annotation is not present on this method
	 * because it is only mandated by Liquibase 3.0.0 and above.
	 * </p>
	 *
	 * @param databaseChangeLog
	 *            the {@link DatabaseChangeLog} that this
	 *            {@link JavaUtilLoggingLogger} will be affiliated with; may be
	 *            {@code null}
	 *
	 * @see #setChangeSet(ChangeSet)
	 *
	 * @since 1.2
	 */
	@Override
	public void setChangeLog(final DatabaseChangeLog databaseChangeLog)
	{
		this.changeLog = databaseChangeLog;
	}

	/**
	 * Returns the {@link DatabaseChangeLog} reference previously set by a call
	 * to the {@link #setChangeLog(DatabaseChangeLog)} method.
	 *
	 * <p>
	 * This method may return {@code null}.
	 * </p>
	 *
	 * @return a {@link DatabaseChangeLog}, or {@code null}
	 *
	 * @since 1.2
	 */
	public DatabaseChangeLog getChangeLog()
	{
		return this.changeLog;
	}

	/**
	 * Returns the {@link ChangeSet} reference previously set by a call to the
	 * {@link #setChangeSet(ChangeSet)} method.
	 *
	 * <p>
	 * This method may return {@code null}.
	 * </p>
	 *
	 * @return a {@link ChangeSet}, or {@code null}
	 *
	 * @since 1.2
	 */
	public ChangeSet getChangeSet()
	{
		return this.changeSet;
	}

	/**
	 * Stores a reference to the supplied {@link ChangeSet}, which might be
	 * helpful for subclasses that wish to include it somehow in the logging
	 * output.
	 *
	 * <h4>Implementation Notes</h4>
	 *
	 * <p>
	 * The {@link Override @Override} annotation is not present on this method
	 * because it is only mandated by Liquibase 3.0.0 and above.
	 * </p>
	 *
	 * <p>
	 * Note that all {@link liquibase.logging.Logger} instances must also
	 * implement the {@code Logger.setChangeLog(DatabaseChangeLog)} method, even
	 * though a {@link DatabaseChangeLog} is reachable from a {@link ChangeSet}
	 * 's {@code ChangeSet.getChangeLog()} method. It is undefined what should
	 * happen if these two {@link DatabaseChangeLog} references differ at
	 * logging time.
	 * </p>
	 *
	 * @param changeSet
	 *            the {@link ChangeSet} that this {@link JavaUtilLoggingLogger}
	 *            will be affiliated with; may be {@code
	 * null}
	 *
	 * @see #setChangeLog(DatabaseChangeLog)
	 *
	 * @since 1.2
	 */
	@Override
	public void setChangeSet(final ChangeSet changeSet)
	{
		this.changeSet = changeSet;
	}

}