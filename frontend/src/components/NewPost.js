import React, {useContext, useState} from 'react';
import {toast} from "../components/ui/use-toast";
import {AuthContext} from "../context/AuthContext";
import {PostContext} from "../context/PostContext";

function NewPost() {
    const [content, setContent] = useState('');
    const [images, setImages] = useState([]);
    const { user, token } = useContext(AuthContext);
    const { setUserPosts } = useContext(PostContext);

    const handleContentChange = (e) => {
        setContent(e.target.value);
    };

    const handleImageUpload = (e) => {
        const selectedImages = Array.from(e.target.files);
        setImages(selectedImages);
    };

    const handlePublishPost = () => {
        const formData = new FormData();
        const postDTO = {
            content: content,
            author: user
        }
        formData.append('postDTO', new Blob([JSON.stringify(postDTO)], { type: "application/json" }));

        images.forEach((image, index) => {
            formData.append(`file`, image);
        });

        fetch('/post/createPost', {
            method: 'POST',
            body: formData,
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => {
                        throw new Error(err.message || 'Unknown error');
                    });
                }
                return response.json();
            })
            .then(data => {
                setUserPosts(data)
                setContent('');
                setImages([]);
            })
            .catch(error => {
                toast({
                    variant: "destructive",
                    title: "Uh oh! Something went wrong.",
                    description: error.message,
                });
            });
    };


    return (
        <div className="w-full bg-white rounded-lg p-6">
            <textarea
                value={content}
                onChange={handleContentChange}
                className="w-full px-3 py-2 text-gray-700 border rounded-lg focus:outline-none focus:shadow-outline mb-4"
                rows="4"
                placeholder="What's on your mind?"
            ></textarea>
            <div className="mb-4">
                {images.length > 0 && (
                    <div className="grid grid-cols-3 gap-2">
                        {images.map((image, index) => (
                            <img
                                key={index}
                                src={URL.createObjectURL(image)}
                                alt={`Selected ${index + 1}`}
                                className="w-full h-auto rounded-lg"
                            />
                        ))}
                    </div>
                )}
            </div>
            <div className="flex justify-between items-center">
                <div className="flex items-center">
                    <label htmlFor="image-upload" className="px-4 py-2 text-white bg-blue-500 rounded-lg shadow-md hover:bg-blue-600 focus:outline-none focus:shadow-outline-blue active:bg-blue-600 cursor-pointer">
                        Add images
                    </label>
                    <input
                        id="image-upload"
                        type="file"
                        multiple
                        accept="image/*"
                        onChange={handleImageUpload}
                        className="hidden"
                    />
                </div>
                <button
                    onClick={handlePublishPost}
                    className="px-4 py-2 text-white bg-green-500 rounded-lg shadow-md hover:bg-green-600 focus:outline-none focus:shadow-outline-green active:bg-green-600"
                >
                    Publish
                </button>
            </div>
        </div>
    );
}

export default NewPost;
