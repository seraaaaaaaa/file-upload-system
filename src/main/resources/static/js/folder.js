$(function () {

    getAllFolder();

    $(document).on('click', '#addFolder', function (e) {
        bootbox.prompt({
            title: "Add Folder",
            inputType: "text",
            centerVertical: true,
            size: "small",
            buttons: {
                confirm: {
                    label: "Add",
                    className: "btn btn-primary"
                },
                cancel: {
                    label: "Cancel",
                    className: "btn btn-secondary"
                }
            },
            callback: function (result) {
                $('.bootbox-input-text').removeClass('border-danger');
                // Check if the input is entered
                if (result == null) {
                    return;
                } else if (result === '') {
                    $('.bootbox-input-text').addClass('border-danger');
                    return false;
                } else {
                    $.ajax({
                        url: '/folder/add',
                        type: 'POST',
                        data: {
                            folderName: result,
                        },
                        success: function (response) {
                            if (response == 'OK') {
                                getAllFolder();
                            } else {
                                bootbox.alert('Failed to add folder: ' + response);
                            }
                        },
                        error: function (xhr, status, error) {
                            bootbox.alert('Error adding folder: ' + error);
                        }
                    });
                }
            }
        });
    });

    $(document).on('click', '.editFolder', function (e) {
        e.stopPropagation();
        var folderId = $(this).data('id');
        var folderName = $(this).data('name');

        bootbox.prompt({
            title: "Edit Folder",
            inputType: "text",
            centerVertical: true,
            size: "small",
            buttons: {
                confirm: {
                    label: "Save",
                    className: "btn btn-primary"
                },
                cancel: {
                    label: "Cancel",
                    className: "btn btn-secondary"
                }
            },
            value: folderName,
            callback: function (result) {
                $('.bootbox-input-text').removeClass('border-danger');
                // Check if the input is entered
                if (result == null) {
                    return;
                } else if (result === '') {
                    $('.bootbox-input-text').addClass('border-danger');
                    return false;
                } else {

                    $.ajax({
                        url: '/folder/edit/' + folderId,
                        type: 'POST',
                        data: {
                            folderName: result
                        },
                        success: function (response) {
                            if (response == 'OK') {
                                getAllFolder();
                            } else {
                                bootbox.alert('Failed to update folder: ' + response);
                            }
                        },
                        error: function (xhr, status, error) {
                            bootbox.alert('Error updating folder: ' + error);
                            //console.error('Error updating folder:', error);
                        }
                    });
                }
            }
        });
    });

    $(document).on('click', '.delFolder', function (e) {
        e.stopPropagation();
        var folderId = $(this).data('id');

        bootbox.confirm({
            title: "Confirm Delete",
            message: "Are you sure you want to delete this folder?",
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
                    // Confirmation is true 
                    $.ajax({
                        type: "DELETE",
                        url: '/folder/delete/' + folderId,
                        success: function (response) {
                            if (response == 'OK') {
                                getAllFolder();
                            } else {
                                bootbox.alert('Failed to delete folder: ' + response);
                            }
                        },
                        error: function (xhr, status, error) {
                            bootbox.alert('Error deleting folder: ' + error);
                        },
                    });
                }
            }
        });
    });

    function getAllFolder() {
        $.ajax({
            url: '/folder/all',
            type: 'GET',
            success: function (data) {
                // Clear existing folder list
                $('#folderList').empty();

                // Append each folder to the folderList div
                data.forEach(function (folder, index) {
                    var folderId = folder.folderId;
                    var folderName = folder.folderName;

                    var folderElement =
                        `<li class="list-group-item cursor-pointer border-bottom folderLink" data-id="${folderId}" data-name="${folderName}">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <i class="fas fa-folder text-warning me-2"></i>
                                    <span>${folderName}</span>
                                </div>
                                <div class="d-flex">
                                    <span class="text-primary editFolder iconBtn" data-id="${folderId}" data-name="${folderName}">
                                        <i class="fas fa-pen"></i>
                                    </span>
                                    <span class="text-danger delFolder iconBtn" data-id="${folderId}">
                                        <i class="fas fa-trash-alt"></i>
                                    </span>
                                </div>
                            </div>
                        </li>`;

                    // Append folderElement to the folderList div
                    $('#folderList').append(folderElement);

                });
            },
            error: function (xhr, status, error) {
                bootbox.alert('Error loading folder: ' + error);
            }
        });
    }

});