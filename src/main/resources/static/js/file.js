

$(function () {

    var fileCropper;
    var currentFileIndex = -1;

    initDropzone();

    initCropper();

    getAllFiles(true);

    $(document).on('click', '.backFolder', function (e) {
        resetFolderView(0, '')

        $('.backFolder').hide();
        $('#folderList,#addFolder').show();
        getAllFiles(null);
    });


    $(document).on('click', '.folderLink', function (e) {
        var folderId = $(this).data('id');
        var folderName = $(this).data('name');

        resetFolderView(folderId, folderName);

        $('.backFolder').show();
        $('#folderList,#addFolder').hide();
        getAllFiles(null);
    });

    $(document).on('click', '.fileLink', function (e) {
        e.preventDefault();
        if ($(e.target).is('i')) {
            return; // Exclude iconbtn from triggering the click event
        }

        $('.list-group-active').removeClass('list-group-active');
        $(this).addClass('list-group-active');
        currentFileIndex = $(this).index();

        var id = $(this).data('id');

        $.ajax({
            url: "/attachment/get/" + id,
            type: 'GET',
            success: function (data) {
                var src = '/file/view/' + data.fileId;

                $('#fileImg,#fileFrame,#fileDownload').parent().hide();

                if (data.file.type.indexOf('pdf') !== -1 || data.file.type.indexOf('text/plain') !== -1) {
                    //$('#fileImg').parent().hide();

                    $("#fileFrame").attr({
                        src: src,
                        width: "100%",
                        height: "640px",
                        type: "application/pdf"
                    });

                    $('#fileFrame').parent().show();
                } else if (data.file.type.indexOf("image/") === 0) {
                    //$('#fileFrame').parent().hide();

                    $('#fileImg').parent().show();
                    $('#fileImg').attr('src', src);
                    $('#fileImg').cropper('destroy');
                    initCropper();
                } else {
                    clearFileView();
                    $('#downloadFile').attr('link', src);
                    $('#fileDownload').parent().show();
                }

            },
            error: function (xhr, status, error) {
                bootbox.alert('Error loading file: ' + error);
            }
        });
    });

    $(document).on('click', '#downloadFile', function (e) {
        var src = $(this).attr('link');

        var downloadLink = $('<a></a>');
        downloadLink.attr('href', src).appendTo('body');
        downloadLink[0].click();
        downloadLink.remove();
    });

    $(document).on('click', '#currentFileBtn', function (e) {
        if (currentFileIndex >= 0) {
            $('#fileList li.fileLink').eq(currentFileIndex).trigger('click').addClass('list-group-active');
        }
    });

    $(document).on('click', '#prevFileBtn', function (e) {
        var fileCount = $('#fileList li.fileLink').length;
        if (fileCount > 1) {
            $('#fileList li.fileLink').removeClass('list-group-active');

            currentFileIndex = (currentFileIndex - 1 + fileCount) % fileCount;
            var prevFile = $('#fileList li.fileLink').eq(currentFileIndex);
            prevFile.trigger('click').addClass('list-group-active');
        }
    });

    $(document).on('click', '#nextFileBtn', function (e) {
        var fileCount = $('#fileList li.fileLink').length;
        if (fileCount > 1) {
            $('#fileList li.fileLink').removeClass('list-group-active');

            currentFileIndex = (currentFileIndex + 1) % fileCount;
            var nextFile = $('#fileList li.fileLink').eq(currentFileIndex);
            nextFile.trigger('click').addClass('list-group-active');
        }
    });

    $(document).on('click', '#rotateLeft', function (e) {
        fileCropper.rotate(-90);
    });

    $(document).on('click', '#rotateRight', function (e) {
        fileCropper.rotate(90);
    });

    $(document).on('click', '#downloadImg', function (e) {
        var imageSource = $('#fileImg').attr('src');
        var downloadLink = $('<a></a>');
        downloadLink.attr('href', imageSource)
            .attr('download', 'image.jpg')
            .appendTo('body');
        downloadLink[0].click();
        downloadLink.remove();
    });


    $(document).on('click', '.moveFile', function (e) {
        e.stopPropagation();
        var id = $(this).data('id');

        $.ajax({
            type: "POST",
            url: "/attachment/move/" + id + "/0",
            success: function (data) {
                getAllFiles(null);
            },
            error: function (xhr, status, error) {
                bootbox.alert('Error moving file: ' + error);
            },

        });
    });

    $(document).on('click', '.delFile', function (e) {
        e.stopPropagation();
        var id = $(this).data('id');

        bootbox.confirm({
            title: "Confirm Delete",
            message: "Are you sure you want to delete this file?",
            centerVertical: true,
            size: "small",
            buttons: {
                confirm: {
                    label: "Yes",
                    className: "btn btn-danger"
                },
                cancel: {
                    label: "No",
                    className: "btn btn-outline-dark"
                }
            },
            callback: function (result) {
                if (result) {
                    $.ajax({
                        type: "DELETE",
                        url: "/attachment/delete/" + id,
                        success: function (response) {
                            if (response == 'OK') {
                                getAllFiles(true);
                                $('.list-group-active').removeClass('list-group-active');
                                clearFileView();
                            } else {
                                bootbox.alert('Failed to delete file: ' + response);
                            }
                        },
                        error: function (xhr, status, error) {
                            bootbox.alert('Error deleting file: ' + error);
                        }
                    });
                }
            }
        });
    });

    function getAllFiles(showFirst) {
        var currentFolderId = $('#currentFolderId').val();

        $.ajax({
            url: "/attachment/all/" + currentFolderId,
            type: 'GET',
            success: function (data) {
                // Clear existing file list
                $('#fileList').empty();

                if (data.length == 0) {
                    $('#noFile').show();
                    $('#viewSection').hide();
                    $('#fileImg,#fileFrame,#fileDownload').parent().hide();
                } else {
                    $('#noFile').hide();
                    $('#viewSection').show();

                    data.forEach(function (file, index) {

                        var id = file.attachmentId;

                        var fileIcon = file.file.type.indexOf('pdf') !== -1 ? '<i class="fas fa-file-pdf text-danger me-2"></i>'
                            : file.file.type.indexOf('text/plain') !== -1 ? '<i class="fas fa-file-alt text-primary me-2"></i>'
                                : '<i class="fas fa-file-image text-info me-2"></i>';

                        var moveBtn = currentFolderId != 0 ? `<span class="text-secondary moveFile iconBtn" data-id="${id}"><i class="fas fa-undo-alt"></i></span>` : '';

                        var delBtn = `<span class="text-danger delFile iconBtn" data-id="${id}"><i class="fas fa-trash-alt"></i></span>`;

                        var fileElement =
                            `<li class="list-group-item cursor-pointer border-bottom fileLink" data-id="${id}">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>${fileIcon} ${file.file.name}</div>
                                    <div class="d-flex">
                                        ${moveBtn}
                                        ${delBtn}
                                    </div>
                                </div>
                            </li>`;

                        $('#fileList').append(fileElement);
                    });

                    setTimeout(() => {
                        initDrag();

                        if (showFirst != null) {
                            if (showFirst || data.length == 1) {
                                currentFileIndex = 0;
                                $('.fileLink').first().trigger('click').addClass('list-group-active');
                            } else {
                                currentFileIndex = data.length - 1;
                                $('.fileLink').last().trigger('click').addClass('list-group-active');
                            }
                        }

                    }, 200);
                }

            },
            error: function (xhr, status, error) {
                bootbox.alert('Error loading files: ' + error);
            }
        });
    }

    function resetFolderView(folderId, folderName) {
        currentFileIndex = -1;

        $('#currentFolderId').val(folderId);
        $('#currentFolderName').text(folderName);
        $('.list-group-active').removeClass('list-group-active');
        clearFileView();
    }

    function clearFileView() {
        $('#fileImg').attr('src', '');
        $('#fileImg').cropper('destroy');

        $("#fileFrame").attr({
            src: '',
            width: "100%",
            height: "640px",
            type: "application/pdf"
        });

        $('#fileImg,#fileFrame,#fileDownload').parent().hide();
    }


    function initCropper() {
        var $image = $('#fileImg');

        $image.cropper({
            aspectRatio: 16 / 9,
            viewMode: 0,
            dragMode: 'move',
            autoCrop: false,
            ready: function (event) {

            },
        });

        // Get the Cropper.js instance after initialized
        fileCropper = $image.data('cropper');
    }

    function initDropzone() {
        $('.dropzone').each(function () {
            let dropzoneControl = $(this)[0].dropzone;
            if (dropzoneControl) {
                dropzoneControl.destroy();
            }
        });

        $('#dropzone').dropzone({
            url: "/attachment/add",
            paramName: "file",
            acceptedFiles: 'application/pdf,image/*,.txt',//,.doc,.docx, 
            previewsContainer: '.previewContainer',
            previewTemplate: $('.preview').html(),
            maxFilesize: 5, // Set the maximum file size to 5MB
            init: function () {

                this.on('sending', function (file, json, formData) {
                    formData.append("folder_id", $('#currentFolderId').val());
                    formData.append("uuid", file.upload.uuid);
                });

                this.on('success', function (file, json) {
                    if (json.result == 'OK') {
                        $('div[data-uid="' + json.uuid + '"] .dz-details .dz-error-message').html('<span class="text-success"><i class="fas fa-check-circle me-2"></i>Uploaded</span>');
                        getAllFiles(false);
                    } else {
                        $('div[data-uid="' + json.uuid + '"] .dz-details .dz-error-message').html('<span class="text-danger"><i class="fas fa-times me-2"></i> ' + json.result + '</span>');
                    }
                });

                this.on('addedfile', function (file) {
                    file.previewElement.setAttribute('data-uid', file.upload.uuid);
                });
            }
        });
    }

    function initDrag() {
        if ($('#currentFolderId').val() == 0) {
            // Make docLink elements draggable
            $("#fileList").sortable({
                update: function (e, ui) {
                    $('#fileList li.fileLink').each(function (i) {
                        var id = $(this).data('id');
                        var order = i + 1;

                        if (id != undefined) {
                            $.ajax({
                                url: "/attachment/order/" + id + "/" + order,
                                type: 'POST',
                                success: function () {
                                },
                                error: function () { }
                            });
                        }
                    });

                    currentFileIndex = $('#fileList li.list-group-active').index();
                    setTimeout(() => {
                        getAllFiles(null);
                    }, 500);
                }
            });

            // Make folderLink elements droppable
            $(".folderLink").droppable({
                accept: ".fileLink", // Only accept draggable elements
                drop: function (event, ui) {
                    // Handle the drop event here
                    var id = ui.draggable.data("id");
                    var folderId = $(this).data("id");

                    // Perform any necessary actions after the drop
                    $.ajax({
                        type: "POST",
                        url: "/attachment/move/" + id + "/" + folderId,
                        success: function (data) {
                            getAllFiles(null);
                        },
                        error: function (xhr, status, error) {
                            bootbox.alert('Error moving file: ' + error);
                        }
                    });
                }
            });
        }
    }

});

