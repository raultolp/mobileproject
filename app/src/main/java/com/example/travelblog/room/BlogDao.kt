package com.example.travelblog.room

import androidx.room.*

@Dao
interface BlogDao {

    //Delete all:
    @Query("DELETE FROM blogs")
    fun deleteAllBlogs()


    //FOR MAIN VIEW:

    //1) Fetch all blogTitles and blogIds (titles for populating the spinner,
    // ids for deciding which blog corresponds to selected blogTitle (same index in list) -
    // selected blog's id should be passed on to Blog Activity):
    @Query("SELECT blogTitle FROM blogs")  //blog titles
    fun loadBlogTitles(): Array<String>

    @Query("SELECT blogId FROM blogs")  //blog ids
    fun loadBlogIds(): Array<Int>

    @Query("SELECT * FROM blogs")  //blogs
    fun loadBlogs(): Array<BlogEntity>


    //2) Fetch all location entities (to decide which locations are nearby):
    @Query("SELECT * FROM blogItems")  // blogitems
    fun loadBlogItems(): Array<BlogItemEntity>


    //--------------------------------------------------------------
    //FOR BLOG ACTIVITY VIEW:

    //Selected blog's id (null in case of new blog) is passed on from 1st to 2nd view
    //Blog item's id (null in case of new unsaved blog item) is passed on from 2nd to 3rd view
    //Also any unsaved info is passed on from 2nd to 3rd view (!!) (so that it can be also saved when
    // Save is pressed)

    //Get selected blog's info (title and description):
    @Query("SELECT * FROM blogs WHERE blogId=:a")  //selected blog
    fun loadSingleBlog(a: Int): BlogEntity

    // Get locations and titles of selected blog's items:
    @Query("SELECT * FROM blogItems WHERE blogIdRef=:a")  //selected blog's items
    fun loadSingleBlogsItems(a: Int): Array<BlogItemEntity>

    //Update Blog:
    @Update
    fun updateBlog(blog: BlogEntity)

    //Update Blog item coordinates (is it needed?):
    @Update
    fun updateBlogItem(blog: BlogItemEntity)

    //Add blog:
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addBlog(blog: BlogEntity)

    //Add blog item (only coordinates can be added here):
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addBlogItem(blog: BlogItemEntity)

    //Delete blog item (before deleting, delete all its items and their entries too)- //TODO: CHECK IF DELETES AUTOMATICALLY!
    //the methods for that are below.
    @Delete
    fun deleteBlog(vararg blog: BlogEntity)


    //--------------------------------------------------------------
    //FOR BLOG ITEM ACTIVITY VIEW:

    //Get blog item title (=place name):
    @Query("SELECT placeName FROM blogItems WHERE blogItemId=:a")  //selected blog
    fun loadPlaceName(a: Int): String?

    //Get all blog item entries of a blog item (photos with descriptions, date and time):
    @Query("SELECT * FROM blogItemEntries WHERE blogItemIdRef=:a")  //selected blog
    fun loadEntries(a: Int): Array<BlogItemEntryEntity>

    //Update blog item title (place name):
    // see above: updateBlogItem(blog: BlogItemEntity)

    //Update blog item entry:
    @Update
    fun updateBlogItemEntry(blog: BlogItemEntryEntity)

    //Add blog item entry (photo, description, datetime)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addBlogItemEntry(blog: BlogItemEntryEntity)

    //Delete blog item (delete its last entry separately before deleting the blog item): //TODO: CHECK IF DELETES AUTOMATICALLY!
    @Delete
    fun deleteBlogItem(vararg item: BlogItemEntity)

    //Delete blog item entry:
    @Delete
    fun deleteBlogItemEntry(vararg entry:  BlogItemEntryEntity) //vararg- means it can be one or several

    //--------------------------------------------------------------

/*    @Transaction
    @Query("SELECT * FROM blogs")
    fun getBlogsWithItems(): List<BlogWithItemsEntity>  //blogs with their blog items
    //Transaction- because actually two queries are made.*/

    //INSERT:
//FROM API: If the @Insert method receives only 1 parameter, it can return a long, which is
// the new rowId for the inserted item. If the parameter is an array or a collection, it
// should return long[] or List<Long> instead.

}

