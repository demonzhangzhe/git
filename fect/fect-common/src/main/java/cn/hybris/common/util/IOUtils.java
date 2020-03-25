package cn.hybris.common.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Qu Dihuai
 *
 */
public class IOUtils {
	/**
	 * Closes a <code>Closeable</code> unconditionally.
	 * <p>
	 * Equivalent to {@link Closeable#close()}, except any exceptions will be
	 * ignored. This is typically used in finally blocks.
	 * </p>
	 * <p>
	 * Example code:
	 * </p>
	 * <p>
	 * 
	 * <pre>
	 * Closeable closeable = null;
	 * try {
	 * 	closeable = new FileReader(&quot;foo.txt&quot;);
	 * 	// process closeable
	 * 	closeable.close();
	 * } catch (Exception e) {
	 * 	// error handling
	 * } finally {
	 * 	IOUtils.closeQuietly(closeable);
	 * }
	 * </pre>
	 * </p>
	 * <p>
	 * Closing all streams:
	 * </p>
	 * <p>
	 * 
	 * <pre>
	 * try {
	 * 	return IOUtils.copy(inputStream, outputStream);
	 * } finally {
	 * 	IOUtils.closeQuietly(inputStream);
	 * 	IOUtils.closeQuietly(outputStream);
	 * }
	 * </pre>
	 * </p>
	 *
	 * @param closeable the objects to close, may be null or already closed
	 */
	public static void close(final Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (final IOException e) {
			}
		}
	}
}
