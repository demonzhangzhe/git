package cn.hybris.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.data.redis.serializer.SerializationException;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

public class SerializationUtils
{
	/**
	 * 序列化
	 * 
	 * @param obj
	 * @return byte[]
	 * @throws IOException 
	 */
	public static byte[] serialize (final Object obj)
	{
		if (obj == null)
		{
			return null;
		}

		try (final ByteArrayOutputStream os = new ByteArrayOutputStream())
		{
			final HessianOutput output = new HessianOutput(os);
			output.writeObject(obj);
			return os.toByteArray();
		} catch (final IOException e) {
			throw new SerializationException("Serialization failed", e);
		}
	}

	/**
	 * 反序列化
	 * @param bytes
	 * @return <T> T
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deserialize(final byte[] bytes)
	{
		if (bytes == null)
		{
			return null;
		}

		try (ByteArrayInputStream is = new ByteArrayInputStream(bytes))
		{
			HessianInput input = new HessianInput(is);
			return (T) input.readObject();
		} catch (final IOException e) {
			throw new SerializationException("Deserialization failed", e);
		}
	}
}
