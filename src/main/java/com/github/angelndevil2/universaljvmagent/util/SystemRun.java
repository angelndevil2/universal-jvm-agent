package com.github.angelndevil2.universaljvmagent.util;

import org.apache.commons.exec.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * To run asynchronously and set result string when process ended, extend {@link ResultHandler}.
 *
 * <pre>{@code
 *
 * public class ExtendedResultHandler extends SystemRun.ResultHandler {
 *
 *  public String result_or_error;
 *
 *  public void onProcessComplete(int exitValue) {
 *      super.onProcessComplete(exitValue);
 *      result_or_error = getResultString();
 *  }
 *
 *  public void onProcessFailed(ExecuteException e) {
 *      super.onProcessFailed(e);
 *      result_or_error = getResultString();
 *  }
 *
 * }
 *
 * }</pre>
 *
 * @author k, Created on 15. 12. 11.
 */
public class SystemRun {

    /**
     * <h3>result handler to execute {@link CommandLine}</h3>
     * process out/err string is reserved in {@link ByteArrayOutputStream output_stream_}
     *
     */
    public static class ResultHandler extends DefaultExecuteResultHandler {

        private ByteArrayOutputStream output_stream_ = new ByteArrayOutputStream();

        /**
         *
         * @return processe's out/err string
         */
        public String getResultString() {
            return output_stream_.toString();
        }

    }

    /**
     * execute {@link CommandLine} and return {@link ResultHandler}
     *
     * @param cmd {@link CommandLine}
     * @return {@link ResultHandler}
     * @throws IOException
     */
    public static ResultHandler execCommand(final CommandLine cmd) throws IOException {

        ResultHandler ret = new ResultHandler();
        execCommand(cmd, 0, ret);

        return ret;
    }

    /**
     * execute {@link CommandLine} and return {@link ResultHandler}
     * when execution end or timed out
     *
     * @param cmd CommandLine
     * @param timeout Process execution time out
     * @return ResultHandler
     * @throws IOException
     */
    public static ResultHandler execCommand(final CommandLine cmd, final long timeout) throws IOException {

        ResultHandler ret = new ResultHandler();
        execCommand(cmd, timeout, ret);

        return ret;
    }

    /**
     * execute command string and return {@link ResultHandler}
     *
     * @param cmd Command string
     * @return {@link ResultHandler}
     * @throws IOException
     */
    public static ResultHandler execCommand(final String cmd) throws IOException {

        CommandLine commnad = CommandLine.parse(cmd);

        return execCommand(commnad);
    }

    /**
     * execute command string and return {@link ResultHandler}
     * when execution end or timed out
     *
     * @param cmd Command string
     * @param timeout Process execution time out
     * @return ResultHandler
     * @throws IOException
     */
    public static ResultHandler execCommand(final String cmd, final long timeout) throws IOException {

        CommandLine commnad = CommandLine.parse(cmd);

        return execCommand(commnad, timeout);
    }

    /**
     * execute asynchronously.<br />
     *
     * if timeout is not set, sub-process may not end. So if use {@link ResultHandler#waitFor()}, may be waiting for infinitely.<br />
     *
     * @param cmd CommandLine
     * @param timeout Process execution time out
     * @param handler ResultHandler
     * @throws IOException
     */
    public static void execCommand(final CommandLine cmd, final long timeout, ResultHandler handler) throws IOException {

        if (timeout > 0) {

            ExecuteWatchdog watchdog = new ExecuteWatchdog(timeout);
            Executor executor = new DefaultExecutor();
            executor.setStreamHandler(new PumpStreamHandler(handler.output_stream_));
            executor.setWatchdog(watchdog);
            executor.execute(cmd, handler);

        } else {

            Executor executor = new DefaultExecutor();
            executor.setStreamHandler(new PumpStreamHandler(handler.output_stream_));
            executor.execute(cmd, handler);

        }
    }
}

