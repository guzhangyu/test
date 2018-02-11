package com.phei.netty.netty.src;

import org.eclipse.core.runtime.Assert;

import java.nio.*;

/**
 * 自定义ByteBuf实现类
 * Created by guzy on 16/8/17.
 */
public class ByteBufImpl{
    
    private ByteBuffer buffer;

    //写下标
    private int writerIndex;

    //读下标
    private int readerIndex;

    //标记的写下标
    private Integer markWriterIndex;

    //标记的读下标
    private Integer markReaderIndex;

    public ByteBufImpl(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    //获取java的ByteBuffer
    public ByteBuffer getBuffer() {
        //buffer.limit()
        return buffer;
    }

    public int writerIndex(){
        return writerIndex;
    }

    //设置写下标
    public ByteBufImpl writerIndex(int writerIndex){
        check(writerIndex,readerIndex);

        this.writerIndex=writerIndex;
        return this;
    }

    public int getReaderIndex() {
        return readerIndex;
    }

    /**
     * 设置读下标
     * @param readerIndex
     * @return
     */
    public ByteBufImpl setReaderIndex(int readerIndex) {
        check(writerIndex,readerIndex);
        this.readerIndex = readerIndex;

        return this;
    }



//    public ByteBufImpl markReaderIndex(int readerIndex){
//        this.markReaderIndex=readerIndex;
//        return this;
//    }

//    public ByteBufImpl markWriterIndex(int writerIndex){
//        this.markWriterIndex=writerIndex;
//        return this;
//    }


    public ByteBufImpl mark(){
        markReaderIndex();
        markWriterIndex();
        return this;
    }

    /**
     * 标记读下标
     * @return
     */
    public ByteBufImpl markReaderIndex(){
        this.markReaderIndex=readerIndex;
        return this;
    }

    public ByteBufImpl markWriterIndex(){
        this.markWriterIndex=writerIndex;
        return this;
    }

    /**
     * 重置读写下标
     * @return
     */
    public ByteBufImpl reset(){
        if(this.markReaderIndex!=null && this.markReaderIndex==this.readerIndex){
            this.markReaderIndex=null;
        }
        if(this.markWriterIndex!=null && this.markWriterIndex==this.writerIndex){
            this.markWriterIndex=null;
        }
        if(this.markReaderIndex==null && this.markWriterIndex==null){
            return this;
        }

        int writerIndex=this.markWriterIndex==null?this.writerIndex:this.markWriterIndex;
        int readerIndex=this.markReaderIndex==null?this.readerIndex:this.markReaderIndex;
        check(writerIndex,readerIndex);

        this.readerIndex=readerIndex;
        this.writerIndex=writerIndex;
        saveBuffer();
        this.markWriterIndex=null;
        this.markReaderIndex=null;

        return this;
    }

    /**
     * 重置读下标
     * @return
     */
    public ByteBufImpl resetReaderIndex(){
        if(markReaderIndex==null){
            return this;
        }
        if(markReaderIndex==readerIndex){
            markReaderIndex=null;
            return this;
        }

        check(writerIndex,markReaderIndex);

        readerIndex=markReaderIndex;
        saveBuffer();
        markReaderIndex=null;
        return this;
    }


    /**
     * 重置写下标
     * @return
     */
    public ByteBufImpl resetWriterIndex(){
        if(markWriterIndex==null){
            return this;
        }
        if(markWriterIndex==writerIndex){
            this.markWriterIndex=null;
            return this;
        }

        check(markWriterIndex,readerIndex);
        writerIndex=markWriterIndex;
        saveBuffer();
        markWriterIndex=null;

        return this;
    }

    /**
     * 检查读写下标是否合法
     * @param writerIndex
     * @param readerIndex
     */
    private void check(int writerIndex,int readerIndex){
        Assert.isTrue( writerIndex>=readerIndex,"writeIndex must larger than readerIndex") ;
        Assert.isTrue(readerIndex>=0,"readerIndex must >=0 ");
        Assert.isTrue(writerIndex<=buffer.capacity(),"writeIndex must <= capacity");
    }

    /**
     * 将读写下标的变化提交到ByteBuffer上
     */
    private void saveBuffer(){
        this.buffer.position(this.readerIndex);
        this.buffer.limit(this.writerIndex);
    }

    /**
     * 重用可读部分
     * @return
     */
    public ByteBufImpl discardReadBytes(){
        //重用后的writeIndex
        writerIndex=writerIndex-readerIndex;

        //数据迁移
        getBytes(0,readerIndex,writerIndex);
        adjustMarks(readerIndex);

        //前置
        readerIndex=0;
        buffer.position(writerIndex);
        return this;
    }

    /**
     * 跳过指定长度的数据
     * @param len
     * @return
     */
    public ByteBufImpl skip(int len){
        check(writerIndex,readerIndex+len);
        readerIndex+=len;
        return this;
    }

    /**
     * 将标记的下标前移
     * @param dec
     */
    private void adjustMarks(int dec){
        this.markReaderIndex-=dec;
        if(markReaderIndex<0){
            markReaderIndex=0;
        }
        markWriterIndex-=dec;
        if(markWriterIndex<0){
            markWriterIndex=0;
        }
    }

    private void getBytes(int dstIndex,int srcIndex,int len){
        if(len!=0){
            byte[]src=new byte[len];
            buffer.get(src,srcIndex,len);
            buffer.put(src,dstIndex,len);
        }
    }

    private final static int dis=1024*1024*4;//阈值

    private final static int maxCapacity=1024*1024*20;//最大容量

    /**
     * 写数据
     * @param dst byte数据
     * @param dstIndex 起始下标
     * @param len 读取长度
     * @return
     */
    public ByteBufImpl write(byte[]dst,int dstIndex,int len){
        Assert.isTrue(dst!=null && dstIndex>0 && len>0 && dst.length>=dstIndex+len,"参数格式不对！");
        int dstWriterIndex=writerIndex+len;
        if(dstWriterIndex>this.buffer.capacity()){//这里有一个问题，就是buffer的position前的部分去掉了，而impl的下标没有变
            addCapacity(dstWriterIndex);
        }
        this.buffer.put(dst,dstIndex,len);
        this.writerIndex+=len;
        return this;
    }

    /**
     * 根据新的容积需求扩容
     * @param dstWriterIndex
     */
    void addCapacity(int dstWriterIndex) {
        if(dstWriterIndex<=buffer.capacity()){
            return;
        }
        if(dstWriterIndex+buffer.position()-1<=buffer.capacity()){
            //this.buffer.flip();
            discardReadBytes();
            return;
        }

        ByteBuffer tmpBuffer=ByteBuffer.allocate(calculateNewCapacity(dstWriterIndex));
        //this.buffer.flip();
        discardReadBytes();
        tmpBuffer.put(this.buffer);
        this.buffer=tmpBuffer;
    }

    private int calculateNewCapacity(int minNewCapacity){
        if(minNewCapacity>maxCapacity){
            throw new IllegalArgumentException("容量不足！");
        }
        int newCapacity=0;
        if(minNewCapacity>dis){
            newCapacity=minNewCapacity/dis*dis+dis;
        }else{
            newCapacity=minNewCapacity*2;
        }
        if(newCapacity>maxCapacity){
            newCapacity=maxCapacity;
        }
        return newCapacity;
    }

    public byte get() {
        byte b= buffer.get();
        readerIndex++;
        return b;
    }


    public ByteBufImpl put(byte b) {
        buffer.put(b);
        writerIndex++;
        return this;
    }

    public byte get(int index) {
        return buffer.get(index);
    }

    public ByteBufImpl put(int index, byte b) {
        buffer.put(index,b);
        return this;
    }


    public ByteBufImpl slice() {
        return null;
    }

    
    public ByteBufImpl duplicate() {
        return null;
    }

      
    public ByteBufImpl asReadOnlyBuffer() {
        return null;
    }
      
    public ByteBufImpl compact() {
        return null;
    }

      
    public boolean isReadOnly() {
        return false;
    }

      
    public boolean isDirect() {
        return false;
    }

      
    public char getChar() {
        return 0;
    }

      
      public ByteBufImpl putChar(char value) {
        return null;
    }

      
    public char getChar(int index) {
        return 0;
    }

      
      public ByteBufImpl putChar(int index, char value) {
        return null;
    }

      
    public CharBuffer asCharBuffer() {
        return null;
    }

      
    public short getShort() {
        return 0;
    }

      
      public ByteBufImpl putShort(short value) {
        return null;
    }

      
    public short getShort(int index) {
        return 0;
    }

      
      public ByteBufImpl putShort(int index, short value) {
        return null;
    }

      
    public ShortBuffer asShortBuffer() {
        return null;
    }

      
    public int getInt() {
        return 0;
    }

      
      public ByteBufImpl putInt(int value) {
        return null;
    }

      
    public int getInt(int index) {
        return 0;
    }

      
      public ByteBufImpl putInt(int index, int value) {
        return null;
    }

      
    public IntBuffer asIntBuffer() {
        return null;
    }

      
    public long getLong() {
        return 0;
    }

      
      public ByteBufImpl putLong(long value) {
        return null;
    }

      
    public long getLong(int index) {
        return 0;
    }

      
      public ByteBufImpl putLong(int index, long value) {
        return null;
    }

      
    public LongBuffer asLongBuffer() {
        return null;
    }

      
    public float getFloat() {
        return 0;
    }

      
      public ByteBufImpl putFloat(float value) {
        return null;
    }

      
    public float getFloat(int index) {
        return 0;
    }

      
      public ByteBufImpl putFloat(int index, float value) {
        return null;
    }

      
    public FloatBuffer asFloatBuffer() {
        return null;
    }

      
    public double getDouble() {
        return 0;
    }

      
      public ByteBufImpl putDouble(double value) {
        return null;
    }

      
    public double getDouble(int index) {
        return 0;
    }

      
      public ByteBufImpl putDouble(int index, double value) {
        return null;
    }

      
    public DoubleBuffer asDoubleBuffer() {
        return null;
    }
}
