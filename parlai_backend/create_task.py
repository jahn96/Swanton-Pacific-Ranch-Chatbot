import os, copy
from csv import reader
from IPython.display import clear_output
from parlai.scripts.train_model import TrainModel
from parlai.scripts.interactive import Interactive
from parlai.scripts.display_data import DisplayData
from parlai.scripts.display_model import DisplayModel
from parlai.core.teachers import register_teacher, DialogTeacher, ParlAIDialogTeacher


# Get data
worksheet_name = 'data/Model.SPR QA Training Data'

# read tsv file as a list of lists
with open(worksheet_name + '.tsv', 'r') as read_obj:
    # pass the file object to reader() to get the reader object
    csv_reader = reader(read_obj, delimiter='\t')
    # Pass reader object to list() to get a list of lists
    rows = list(csv_reader)

# Convert data to ParlAI Dialogue format
with open(worksheet_name + ".txt", 'w') as f:
    for j in range(len(rows)):
        for i in range(0, len(rows[j]), 2):
            if rows[j][i] == "":
                break
            if j > 0:
                f.write("\n")
            f.write("text:")
            f.write(rows[j][i])
            f.write("\t")
            f.write("labels:")
            if i < len(rows[j]):
                f.write(rows[j][i + 1])
        f.write("\tepisode_done:True")

def _path(opt, filtered):
    # set up path to data (specific to each dataset)
    return worksheet_name + ".txt"

@register_teacher("spr_teacher")
class SPRTeacher(ParlAIDialogTeacher):
    def __init__(self, opt, shared=None):
        opt = copy.deepcopy(opt)

        # get datafile
        opt['parlaidialogteacher_datafile'] = _path(opt, '')

        super().__init__(opt, shared)


DisplayData.main(task="spr_teacher")
#
#
# TrainModel.main(
#   # similar to before
#   task='spr_teacher',
#   model='transformer/generator',
#   model_file='transfer_base/model',
#
#   # initialize with a pretrained model
#   init_model='zoo:blender/blender_90M/model',
#
#   # arguments we get from the pretrained model.
#   # Unfortunately, these must be looked up separately for each model.
#   n_heads=16, n_layers=8, n_positions=512, text_truncate=512,
#   label_truncate=128, ffn_size=2048, embedding_size=512,
#   activation='gelu', variant='xlm',
#   dict_lower=True, dict_tokenizer='bpe',
#   dict_file='zoo:blender/blender_90M/model.dict',
#   learn_positional_embeddings=True,
#
#   # some training arguments, specific to this fine-tuning
#   # use a small learning rate with ADAM optimizer
#   lr=1e-5, optimizer='adam',
#   warmup_updates=100,
#   # early stopping on perplexity
#   validation_metric='ppl',
#   # train at most 10 minutes, and validate every 0.25 epochs
#   max_train_time=600, validation_every_n_epochs=0.25,
#
#   # depend on your gpu. If you have a V100, this is good
#   batchsize=12, fp16=True, fp16_impl='mem_efficient',
#
#   # speeds up validation
#   skip_generation=True,
#
#   # helps us cram more examples into our gpu at a time
#   dynamic_batching='full',
# )

# DisplayModel.main(
#     task='spr_teacher',
#     model='parlai_backend/model',
#     num_examples=2,
#     # skip_generation=False,
# )