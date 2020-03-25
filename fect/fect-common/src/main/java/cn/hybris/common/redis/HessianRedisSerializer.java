package cn.hybris.common.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

/**
 * 缓存值序列化与反序列化
 * 
 * @author Qu Dihuai
 * @param <T>
 */
public class HessianRedisSerializer<T> implements RedisSerializer<T>
{
	@Override
	public byte[] serialize(final T t) throws SerializationException
	{
		if (t == null)
		{
			throw new NullPointerException("The object must be not null");
		}

		try (final ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			final HessianOutput output = new HessianOutput(baos);
			output.writeObject(t);
			return baos.toByteArray();
		}
		catch (Exception e)
		{
			throw new SerializationException("Serialize object by Hessian failed", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(final byte[] bytes) throws SerializationException
	{
		if (bytes == null)
		{
			throw new NullPointerException("The bytes must be not null");
		}

		try (final ByteArrayInputStream bais = new ByteArrayInputStream(bytes))
		{
			final HessianInput input = new HessianInput(bais);
			return (T) input.readObject();
		}
		catch (Exception e)
		{
			throw new SerializationException("Deserialize object by Hessian failed", e);
		}
	}
}
