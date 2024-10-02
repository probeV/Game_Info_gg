// S3 File 생성 API
export function createFile(directoryPath, selectedImageFile){
    const formData = new FormData();

    const FileSaveRequestDto = {
        directoryPath: directoryPath
    };

    formData.append('file', selectedImageFile);
    formData.append('FileSaveRequestDto', new Blob([JSON.stringify(FileSaveRequestDto)], { type: "application/json" }));

    // S3 파일 생성
    return $.ajax({
        url: `/api/v1/admins/files`,
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function(response) {
            console.log('File created successfully:', response);
            return response;
        },
        error: function(error) {
            console.error('Error creating file:', error);
            alert('파일 생성 중 오류가 발생했습니다.')
        }
    });
}

// S3 File 수정 API
export function updateFile(preFileUrl, directoryPath, selectedImageFile){
    const formData = new FormData();

    const FileUpdateRequestDto = {
        preFileUrl: preFileUrl,
        directoryPath: directoryPath
    };

    formData.append('file', selectedImageFile);
    formData.append('FileUpdateRequestDto', new Blob([JSON.stringify(FileUpdateRequestDto)], { type: "application/json" }));

    // S3 파일 수정
    return $.ajax({
        url: `/api/v1/admins/files`,
        type: 'PUT',
        data: formData,
        processData: false,
        contentType: false,
        success: function(response) {
            console.log('File updated successfully:', response);
            return response;
        },
        error: function(error) {
            console.error('Error updating file:', error);
            alert('파일 수정 중 오류가 발생했습니다.')
        }
    });
}

// S3 File 삭제 API
export function deleteFile(preFileUrl){
    const FileDeleteRequestDto = {
        imageUrl: preFileUrl
    };

    $.ajax({
        url: `/api/v1/admins/files`,
        type: 'DELETE',
        contentType: 'application/json',
        data: JSON.stringify(FileDeleteRequestDto),
        success: function (response) {

        },
        error: function (error) {
            console.error('Error deleting item:', error);
            alert('파일 삭제 중 오류가 발생했습니다.')
        }
    })
}