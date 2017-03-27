package com.tag.framework.io;

import java.io.UnsupportedEncodingException;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class DynamicByteBuffer
{
  static final int DEFAULT_INITIAL_CAPACITY = 256;
  static final int MINIMUM_CAPACITY = 16;
  static final int MAXIMUM_CAPACITY = 1073741824;
  static final int MAXIMUM_LOOP_TIMES = 20;
  private ByteBuffer byteBuffer;

  private DynamicByteBuffer()
  {
  }

  public DynamicByteBuffer(ByteBuffer byteBuffer)
  {
    this.byteBuffer = byteBuffer;
  }

  public static DynamicByteBuffer allocate() {
    DynamicByteBuffer buffer = new DynamicByteBuffer();
    buffer.byteBuffer = ByteBuffer.allocate(256);
    return buffer;
  }

  public static DynamicByteBuffer allocate(int capacity) {
    DynamicByteBuffer buffer = new DynamicByteBuffer();
    if (capacity < 16) {
      capacity = 16;
    }
    buffer.byteBuffer = ByteBuffer.allocate(capacity);
    return buffer;
  }

  public byte get() {
    return this.byteBuffer.get();
  }

  public boolean getBoolean() {
    byte b = this.byteBuffer.get();
    boolean bool = b == 1;
    return bool;
  }

  public char getChar() {
    return this.byteBuffer.getChar();
  }

  public double getDouble() {
    return this.byteBuffer.getDouble();
  }

  public float getFloat() {
    return this.byteBuffer.getFloat();
  }

  public int getInt() {
    return this.byteBuffer.getInt();
  }

  public long getLong() {
    return this.byteBuffer.getLong();
  }

  public short getShort() {
    return this.byteBuffer.getShort();
  }

  public ByteBuffer put(byte b)
  {
    int len = this.byteBuffer.remaining();
    if (len < 1) {
      this.byteBuffer.flip();
      byte[] bb = new byte[this.byteBuffer.limit()];
      this.byteBuffer.get(bb);
      this.byteBuffer = ByteBuffer.allocate(this.byteBuffer.capacity() << 1);
      this.byteBuffer.put(bb);
    }
    return this.byteBuffer.put(b);
  }

  public ByteBuffer putBoolean(boolean bool)
  {
    byte b = (byte)(bool ? 1 : 0);
    return put(b);
  }

  public ByteBuffer putChar(char value) {
    int len = this.byteBuffer.remaining();
    if (len < 1) {
      this.byteBuffer.flip();
      byte[] bb = new byte[this.byteBuffer.limit()];
      this.byteBuffer.get(bb);
      this.byteBuffer = ByteBuffer.allocate(this.byteBuffer.capacity() << 1);
      this.byteBuffer.put(bb);
    }
    return this.byteBuffer.putChar(value);
  }

  public ByteBuffer putDouble(double value) {
    int len = this.byteBuffer.remaining();
    if (len < 8) {
      this.byteBuffer.flip();
      byte[] bb = new byte[this.byteBuffer.limit()];
      this.byteBuffer.get(bb);
      this.byteBuffer = ByteBuffer.allocate(this.byteBuffer.capacity() << 1);
      this.byteBuffer.put(bb);
    }
    return this.byteBuffer.putDouble(value);
  }

  public ByteBuffer putFloat(float value) {
    int len = this.byteBuffer.remaining();
    if (len < 4) {
      this.byteBuffer.flip();
      byte[] bb = new byte[this.byteBuffer.limit()];
      this.byteBuffer.get(bb);
      this.byteBuffer = ByteBuffer.allocate(this.byteBuffer.capacity() << 1);
      this.byteBuffer.put(bb);
    }
    return this.byteBuffer.putFloat(value);
  }

  public ByteBuffer putInt(int value) {
    int len = this.byteBuffer.remaining();
    if (len < 4) {
      this.byteBuffer.flip();
      byte[] bb = new byte[this.byteBuffer.limit()];
      this.byteBuffer.get(bb);
      this.byteBuffer = ByteBuffer.allocate(this.byteBuffer.capacity() << 1);
      this.byteBuffer.put(bb);
    }
    return this.byteBuffer.putInt(value);
  }

  public ByteBuffer putLong(long value) {
    int len = this.byteBuffer.remaining();
    if (len < 8) {
      this.byteBuffer.flip();
      byte[] bb = new byte[this.byteBuffer.limit()];
      this.byteBuffer.get(bb);
      this.byteBuffer = ByteBuffer.allocate(this.byteBuffer.capacity() << 1);
      this.byteBuffer.put(bb);
    }
    return this.byteBuffer.putLong(value);
  }

  public ByteBuffer putShort(short value) {
    int len = this.byteBuffer.remaining();
    if (len < 2) {
      this.byteBuffer.flip();
      byte[] bb = new byte[this.byteBuffer.limit()];
      this.byteBuffer.get(bb);
      this.byteBuffer = ByteBuffer.allocate(this.byteBuffer.capacity() << 1);
      this.byteBuffer.put(bb);
    }
    return this.byteBuffer.putShort(value);
  }

  public final int position() {
    return this.byteBuffer.position();
  }

  public final Buffer position(int newPosition) {
    return this.byteBuffer.position(newPosition);
  }

  public final int capacity() {
    return this.byteBuffer.capacity();
  }

  public final int limit() {
    return this.byteBuffer.limit();
  }

  public final Buffer flip() {
    return this.byteBuffer.flip();
  }

  public final ByteBuffer put(byte[] src) {
    int srcLen = src.length;
    int len = this.byteBuffer.remaining();
    if (len < srcLen) {
      int position = this.byteBuffer.position();
      int capacity = this.byteBuffer.capacity();

      for (int i = 0; (i < 20) && 
        (capacity - position < srcLen); i++)
      {
        capacity <<= 1;
      }

      this.byteBuffer.flip();
      byte[] bb = new byte[this.byteBuffer.limit()];
      this.byteBuffer.get(bb);
      this.byteBuffer = ByteBuffer.allocate(capacity);
      this.byteBuffer.put(bb);
    }

    return this.byteBuffer.put(src);
  }

  public ByteBuffer get(byte[] dst) {
    return this.byteBuffer.get(dst);
  }

  public void putString(String str)
  {
    byte[] bb = null;
    try {
      bb = str.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    short len = (short)bb.length;
    putShort(len);
    put(bb);
  }

  public String getString(ByteBuffer byteBuffer)
  {
    short len = byteBuffer.getShort();
    byte[] bb = new byte[len];
    byteBuffer.get(bb);
    String str = null;
    try {
      str = new String(bb, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return str;
  }

  public byte[] getBytes()
  {
    this.byteBuffer.flip();
    byte[] bb = new byte[this.byteBuffer.limit()];
    this.byteBuffer.get(bb);
    return bb;
  }

  public String toString()
  {
    return this.byteBuffer.toString();
  }
}